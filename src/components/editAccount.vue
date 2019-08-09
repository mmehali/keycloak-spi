<template>
  <div>
    <form class="edit-profile" @submit.prevent="handleSubmit">
      <h1>Edit Account</h1>
      <hr />
      <div class="field">
        <label class="label">Username</label>
        <div class="control has-icons-left has-icons-right">
          <input
            class="input is-primary"
            name="Username"
            type="text"
            placeholder="Username"
            data-vv-as="username"
            v-validate="'required|min:3'"
            v-model="form.Username"
            :class="{ 'input is-danger': errors.has('Username') }"
            disabled
          />
          <span class="icon is-small is-left">
            <font-awesome-icon icon="user" />
          </span>
          <span
            v-if="form.Username !== null && !errors.has('Username')"
            class="icon is-small is-right"
          >
            <font-awesome-icon icon="check" />
          </span>
          <span v-if="errors.has('Username')" class="icon is-small is-right">
            <font-awesome-icon icon="exclamation-triangle" />
          </span>
          <span class="err-msg">{{ errors.first('Username') }}</span>
        </div>
      </div>
      <div class="field">
        <label class="label">Email</label>
        <div class="control has-icons-left has-icons-right">
          <input
            class="input is-primary"
            name="Email"
            type="email"
            placeholder="Email"
            v-validate="'required|email'"
            data-vv-as="email"
            v-model="form.Email"
            :class="{ 'input is-danger': errors.has('Email') }"
          />
          <span class="icon is-small is-left">
            <font-awesome-icon icon="envelope" />
          </span>
          <span v-if="form.Email !== null && !errors.has('Email')" class="icon is-small is-right">
            <font-awesome-icon icon="check" />
          </span>
          <span v-if="errors.has('Email')" class="icon is-small is-right">
            <font-awesome-icon icon="exclamation-triangle" />
          </span>
          <span class="err-msg">{{ errors.first('Email') }}</span>
        </div>
      </div>
      <div class="field">
        <label class="label">First Name</label>
        <div class="control has-icons-right">
          <input
            class="input is-primary"
            name="firstName"
            type="text"
            placeholder="First Name"
            data-vv-as="firstname"
            v-validate="{ required: true, regex: /^([^0-9]*)$/ , min:2}"
            v-model="form.FirstName"
            :class="{ 'input is-danger': errors.has('firstName') }"
          />
          <span
            v-if="form.FirstName !== null && !errors.has('firstName')"
            class="icon is-small is-right"
          >
            <font-awesome-icon icon="check" />
          </span>
          <span v-if="errors.has('firstName')" class="icon is-small is-right">
            <font-awesome-icon icon="exclamation-triangle" />
          </span>
          <span class="err-msg">{{ errors.first('firstName') }}</span>
        </div>
      </div>
      <div class="field">
        <label class="label">Last Name</label>
        <div class="control has-icons-right">
          <input
            class="input is-primary"
            name="lastName"
            type="text"
            placeholder="Last Name"
            data-vv-as="lastname"
            v-validate="{ required: true, regex: /^([^0-9]*)$/ , min:2}"
            v-model="form.LastName"
            :class="{ 'input is-danger': errors.has('lastName') }"
          />
          <span
            v-if="form.LastName !== null && !errors.has('lastName')"
            class="icon is-small is-right"
          >
            <font-awesome-icon icon="check" />
          </span>
          <span v-if="errors.has('lastName')" class="icon is-small is-right">
            <font-awesome-icon icon="exclamation-triangle" />
          </span>
          <span class="err-msg">{{ errors.first('lastName') }}</span>
        </div>
      </div>
      <div align="right">
        <button type="Submit" class="button is-primary">Save</button>
      </div>
    </form>
    <button class="button" v-on:click="logout">Logout</button>
  </div>
</template>

<script>
import { keycloak } from "../main";
import { updateInfo, getGroup } from "../restFunction";
export default {
  name: "editAccount",
  data() {
    return {
      form: {
        Username: null,
        Email: null,
        FirstName: null,
        LastName: null
      },
      submitted: false
    };
  },
  mounted() {
    setTimeout(() => {
      this.editAcc();
    }, 1500);
  },
  methods: {
    updateInfo: function() { //Rest Endpoint UpdateForm
      updateInfo(this.form);
      location.reload();
    },
    editAcc: function() { //Function GetGroup in Keycloak
      keycloak
        .loadUserProfile()
        .success(profile => {
          this.form.Username = profile.username;
          this.form.FirstName = profile.firstName;
          this.form.LastName = profile.lastName;
          this.form.Email = profile.email;
        })
        .error(() => {
          alert("Failed to load user profile");
        });
    },
    logout: function() {
      keycloak.logout();
    },
    handleSubmit: function() { //Function SubmitForm
      this.submitted = true;
      this.$validator.validate().then(valid => {
        if (valid) {
          alert("SUCCESS!! :-)\n\n" + JSON.stringify(this.form));
          this.updateInfo(); 
        }
      });
    }
  }
};
</script>

