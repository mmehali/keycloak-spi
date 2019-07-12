<template>
  <div>
    mygroup
    <div>{{this.group}}</div>
    <button class="button" v-on:click="leaveGroupFunc">Leave: MECAs</button>
    <button class="button" v-on:click="TESTBTN">TEST BTN</button>
  </div>
</template>
<script>
import axios from "axios";
import { keycloak } from "../main";
import { getGroup, leaveGroup } from "../restFunction";
export default {
  name: "mygroup",
  data() {
    return {
      group: ""
    };
  },
  mounted() {
    setTimeout(() => {
      this.showGroup();
    }, 1000);
  },
  methods: {
    leaveGroupFunc: function() {
      leaveGroup("MECAs");
    },
    TESTBTN: function() {
      getGroup().then(data => {
        alert(data);
      });
    },
    showGroup: function() {
      keycloak
        .updateToken(300)
        .success(() => {
          getGroup().then(data => {
            this.group = data;
          });
        })
        .error(() => {
          alert("Failed to refresh token");
        });
    } 
  }
};
</script>
