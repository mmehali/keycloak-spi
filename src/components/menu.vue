<template>
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
          <p class="menu-label">Another Function</p>
          <ul class="menu-list">
            <li>
              <a class="haha">Function 1</a>
            </li>
            <li>
              <a >Function 2</a>
              <ul>
                <li>
                  <a>Function 2.1</a>
                </li>
                <li>
                  <a>Function 2.2</a>
                </li>
                <li>
                  <a>Function 2.3</a>
                </li>
              </ul>
            </li>
            <li>
              <a>Function 3</a>
            </li>
            <li>
              <a>Function 4</a>
            </li>
            <li>
              <a>Function 5</a>
            </li>
          </ul>
          
        </aside>
      </div>
</template>
<script>
export default {
    name: "menu",
}
</script>

