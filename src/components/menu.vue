<template>
  <!-- Menu Left Side -->
  <div class="column is-2 is-sidebar-menu is-hidden-mobile">
    <aside class="menu">
      <p class="menu-label">Manage Account</p>
      <ul class="menu-list">
        <li>
          <router-link exact-active-class="is-active has-background-primary" to="/">Account</router-link>
        </li>
        <li>
          <router-link exact-active-class="is-active has-background-primary" to="/editPassword">password</router-link>
        </li>
        <li>
          <router-link exact-active-class="is-active has-background-primary" to="/mygroup">mygroup</router-link>
        </li>
        <li>
          <a>Authenticator</a>
        </li>
        <li>
          <a>Sessions</a>
        </li>
      </ul>
    </aside>
  </div>
</template>
<script>
export default {
  name: "menu"
};
</script>

