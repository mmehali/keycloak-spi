import axios from "axios";
import { keycloak } from "./main";
// const authUrl ='https://keycloak.demo.web.meca.in.th/auth'
// const realmUrl = '/dev'
var getGroup = function() {
  return axios({
    method: "GET",
    url: "http://localhost:8080/auth/realms/test/ino/mygroup",
    credentials: "include",
    headers: {
      Accept: "text/plain",
      "Content-Type": "text/plain",
      Authorization: "Bearer " + keycloak.token
    }
  })
    .then(resData => {
      return resData.data;
    })
    .catch(err => {
      alert(err);
    });
};

var leaveGroup = function(group) {
  axios({
    method: "POST",
    url: "http://localhost:8080/auth/realms/test/ino/mygroup",
    credentials: "include",
    headers: {
      Accept: "text/plain",
      "Content-Type": "text/plain",
      Authorization: "Bearer " + keycloak.token
    },
    data: group
  })
    .then(res => {
      alert(res.data);
    })
    .catch(err => {
      alert(err);
    });
};

var updateInfo = function(form) {
  axios({
    method: "POST",
    url: keycloak.createAccountUrl().split("?")[0],
    credentials: "include",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization: "Bearer " + keycloak.token
    },
    data: {
      email: form.Email,
      firstName: form.FirstName,
      lastName: form.LastName
    }
  }).catch(err => {
    alert(err);
  });
};

var updatePass = function(form) {
  axios({
    method: "POST",
    url:
      "https://keycloak.demo.web.meca.in.th/auth/realms/dev/account/password",
    credentials: "include",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/x-www-form-urlencoded",
      Authorization: "Bearer " + keycloak.token
    },
    data: {
      username: form.Username,
      password: form.Password,
      "password-new": form.newPassword,
      "password-confirm": form.Confirmation
    }
  }).catch(err => {
    alert(err);
  });
};

export { updateInfo, updatePass, getGroup, leaveGroup };
