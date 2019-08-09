<template>
  <div>
    <form class="edit-profile" @submit.prevent="handleSubmit">
      <h1>Edit Password</h1>
      <hr>
      <div class="field">
        <label class="label">Password</label>
        <div class="control has-icons-right">
          <input
            class="input is-primary"
            name="Password"
            type="password"
            placeholder="Password"
            data-vv-as="Password"
            v-validate="'required|min:4|max:16'"
            v-model="form.Password"
            :class="{ 'input is-danger': errors.has('Password') }"
          >
          <span
            v-if="form.Password !== null && !errors.has('Password')"
            class="icon is-small is-right"
          >
            <font-awesome-icon icon="check"/>
          </span>
          <span v-if="errors.has('Password')" class="icon is-small is-right">
            <font-awesome-icon icon="exclamation-triangle"/>
          </span>
          <span class="err-msg">{{ errors.first('Password') }}</span>
        </div>
      </div>
      <div class="field">
        <label class="label">New Password</label>
        <div class="control has-icons-right">
          <input
            class="input is-primary"
            name="newPassword"
            type="password"
            placeholder="New Password"
            v-validate="'required|min:4|max:16'"
            data-vv-as="New Password"
            v-model="form.newPassword"
            :class="{ 'input is-danger': errors.has('newPassword') }"
            ref="password"
          >
          <span
            v-if="form.newPassword !== null && !errors.has('newPassword')"
            class="icon is-small is-right"
          >
            <font-awesome-icon icon="check"/>
          </span>
          <span v-if="errors.has('newPassword')" class="icon is-small is-right">
            <font-awesome-icon icon="exclamation-triangle"/>
          </span>
          <span class="err-msg">{{ errors.first('newPassword') }}</span>
        </div>
      </div>
      <div class="field">
        <label class="label">Confirmation</label>
        <div class="control has-icons-right">
          <input
            class="input is-primary"
            name="Confirmation"
            type="password"
            placeholder="Confirm Password"
            data-vv-as="Confirmation"
            v-validate="'required|confirmed:password'"
            v-model="form.Confirmation"
            :class="{ 'input is-danger': errors.has('Confirmation') }"
          >
          <span
            v-if="form.Confirmation !== null && !errors.has('Confirmation')"
            class="icon is-small is-right"
          >
            <font-awesome-icon icon="check"/>
          </span>
          <span v-if="errors.has('Confirmation')" class="icon is-small is-right">
            <font-awesome-icon icon="exclamation-triangle"/>
          </span>
          <span class="err-msg">{{ errors.first('Confirmation') }}</span>
        </div>
      </div>
      <div align="right">
        <button type="Submit" class="button is-primary">Save</button>
      </div>
    </form>
  </div>
</template>

<script>
import { keycloak } from "../main";
import { updatePass } from "../restFunction";
export default {
  name: "editPassword",
  data() {
    return {
      form: {
        Username: null,
        Password: null,
        newPassword: null,
        Confirmation: null
      },
      submitted: false
    };
  },
  mounted() {
    setTimeout(() => {
      this.getUser();
    }, 1000);
  },
  methods: {
    updatePass:function() { //Rest Endpoint Update Password **Cors Ploblem**
       updatePass(this.form);
    },
    getUser:function() { //Function GetUser Form Keycloak
      keycloak
        .loadUserProfile()
        .success(profile => {
          this.form.Username = profile.username;
        })
        .error(() => {
          alert("Failed to load user profile");
        });
    },
    handleSubmit:function() { //Function SubmitForm
      this.submitted = true;
      this.$validator.validate().then(valid => {
        if (valid) {
          alert("SUCCESS!! :-)\n\n" + JSON.stringify(this.form));
            this.updatePass();
        }
      });
    }
  }
};
</script>