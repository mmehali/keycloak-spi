<template>
  <div>
    <form class="edit-profile">
      <section class="hero is-primary">
        <div class="hero-body">
          <div class="container">
            <h1 class="title">MY GROUP</h1>
          </div>
        </div>
      </section>
      <!-- Get Group -->
      <div class="container" v-for="groupp in groupsName" :key="groupp">
        <div class="right">
          <!-- Invite user and generate token link -->
          <a
            class="button is-primary"
            style="width:120px; height:55px;"
            v-on:click="isHiddenCheck(groupp)"
          >Invite</a>
          <!-- Leave Group -->
          <a
            class="button is-danger"
            style="width:120px; height:55px;"
            v-on:click="leaveGroupFunc(groupp)"
          >Leave</a>
        </div>
        <div class="left">
          <div class="card" style="margin:10px; width:90%">
            <header class="card-header">
              <p class="card-header-title" :value="groupp">{{groupp}}</p>
            </header>
            <div class="card-content">
              <div class="content">
                <div>
                  <!-- Get members in group -->
                  {{getMembers(groupp)}}
                  <div v-for="member in groupMembers" :key="member">
                    <span class="icon is-small is-left">
                      <font-awesome-icon icon="user" />
                    </span>
                    {{member}}
                  </div>
                </div>
                <br />
              </div>
            </div>
          </div>
        </div>
        <div
          class="field is-grouped"
          style="margin:10px;"
          v-if="isHidden.isHidden===true&&isHidden.groupName===groupp"
        >
          <input class="input" type="text" v-model="isHidden.nameUserInvite" style="width:40%" />
          <a
            class="button is-warning"
            style="marginTop:0px;"
            v-on:click="getToken(groupp,isHidden.nameUserInvite)"
          >Invite</a>
          <input
            v-if="token!==null"
            style="marginLeft:10px;"
            class="input"
            name="token"
            v-model="token"
          />
        </div>
        <hr />
      </div>
    </form>
  </div>
</template>
<script>
import { keycloak } from "../main";
import {
  getGroup,
  leaveGroup,
  getInviteToken
} from "../restFunction";
export default {
  name: "mygroup",
  data() {
    return {
      groupData: null, // Json form rest endpoint [ GroupId, GroupName,members[] ]
      groupsName: [], // Array of groupsName in keycloak
      groupMembers: [], //Array of groupMember in group
      token: null, // token After generate function:getInviteTokenFunc
      isHidden: { //when click inviteUser button
        isHidden: false,
        groupName: null,
        nameUserInvite: null
      }
    };
  },
  mounted() {
    setTimeout(() => {
      this.showGroup();
    }, 1000);
  },
  methods: {
    getToken: function(groupp, nameUserInvite) { //Function call rest endpoint getToken
      var groupId = null;
      for (var i = 0; i < this.groupData.length; i++) {
        if (this.groupData[i].name === groupp) {
          groupId = this.groupData[i].id;
        }
      }
      this.getInviteTokenFunc(groupId, nameUserInvite);
    },
    getInviteTokenFunc: function(groupId, nameUserInvite) { //rest endpoint getToken // send groupId and username of user to generate Token
      getInviteToken(groupId, nameUserInvite).then(data => {
        if (data === undefined) {
          this.token = null;
        } else {
          this.token = "http://localhost:3000/inviteuser?key=" + data; 
        }
      });
    },
    leaveGroupFunc: function(groupp) { //Function LeaveGroup
      leaveGroup(groupp);
      setTimeout(() => {
        location.reload();
      }, 1000);
    },
    showGroup: function() { //Function GetGroup Form Keycloak
      keycloak
        .updateToken(300)
        .success(() => {
          getGroup().then(data => { //rest Endpoint getGroup
            this.groupData = data; //Set Json Group form keycloak
            this.getMembers(); 
            this.show();
          });
        })
        .error(() => {
          alert("Failed to refresh token");
        });
    },
    show: function() { //Function show group to screen
      for (var i = 0; i < this.groupData.length; i++) {
        this.groupsName.push(this.groupData[i].name);
      }
    },
    getMembers: function(groupp) { //Function show members to screen
      for (var i = 0; i < this.groupData.length; i++) {
        if (groupp === this.groupData[i].name) {
          this.groupMembers = this.groupData[i].members;
        }
      }
    },
    isHiddenCheck: function(groupp) { //Function check isHidden or not when click inviteUser
      this.isHidden.isHidden = true;
      this.isHidden.groupName = groupp;
      this.isHidden.nameUserInvite = null;
      this.token = null;
    }
  }
};
</script>
