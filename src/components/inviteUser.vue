<template>
  <div>
    <!-- User have permission to join this group -->
    <form v-if="checkPermission===true" class="edit-profile"> 
       <section class="hero is-primary">
        <div class="hero-body">
          <div class="container">
            <h1 class="title">GROUP INVITE</h1>
          </div>
        </div>
      </section>
      <div class="card" style="margin:10px;">
          <header class="card-header">
            <p class="card-header-title">{{invite.groupInvite}}</p>
            <a href="#" class="card-header-icon" aria-label="more options">
              <span class="icon">
                <i class="fas fa-angle-down" aria-hidden="true"></i>
              </span>
            </a>
          </header>
          <div class="card-content">
            <div class="content">
              (DISCRIPTION) INO Group
              <br />
            </div>
          </div>
          <footer class="card-footer">
            <a
              class="card-footer-item"
              style="backgroundColor:hsl(217, 71%,  53%); color:white; fontSize:18px"
              v-on:click="processInviteTokenFunc(token)"
            >Join</a>
            <router-link
              class="card-footer-item"
              style="fontSize:18px"
              to="/mygroup"
            >Cancel</router-link>
          </footer>
        </div>
    </form>
    <!-- User Dont have permission to join this Group -->
    <form v-if="checkPermission===false" class="edit-profile"> 
      <article class="message is-danger">
        <div class="message-body">
          <strong>{{userLogin}}</strong> Don't have permission to join <strong>{{invite.groupInvite}} Group</strong>
        </div>
      </article>
    </form>
  </div>
</template>
<script>
import { keycloak } from "../main";
import { processInviteToken } from "../restFunction";
export default {
  name: "inviteUser",
  data() {
    return {
      token: this.$route.query.key, //token get form query params
      myJson: null, //Json when decode JWT
      invite: {
        userInvite: null,
        groupInvite: null
      },
      userLogin: null,
      checkPermission: null
    };
  },
  mounted() {
    this.parseJwt(this.$route.query.key);
    setTimeout(() => {
      this.getUser();
      setTimeout(() => {
        this.checkpermission();
      }, 1000);
    }, 1000);
  },
  methods: {
    processInviteTokenFunc: function(token) { //Rest endpoint join group
      processInviteToken(token);
      window.location='http://localhost:3000/mygroup';
    },
    parseJwt: function(token) { //Function decode JWT to Json
      var base64Url = token.split(".")[1];
      var base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
      var jsonPayload = decodeURIComponent(
        atob(base64)
          .split("")
          .map(function(c) {
            return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
          })
          .join("")
      );
      this.myJson = JSON.parse(jsonPayload);
      this.invite.userInvite = this.myJson.username; 
      this.invite.groupInvite = this.myJson.groupName;
    },
    getUser:function() { //Function Get UserLogin to check permission
      keycloak
        .loadUserProfile()
        .success(profile => {
          this.userLogin = profile.username;
        })
        .error(() => {
          alert("Failed to load user profile");
        });
    },
    checkpermission: function() { //Function Check permission
      if (this.userLogin === this.invite.userInvite) {
        this.checkPermission = true;
      } else {
        this.checkPermission = false;
      }
    }
  }
};
</script>

