<template>
  <div>
    <div class="columns is-fullheight">
      <Menu></Menu>
      <div class="column is-main-content">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<script>
import menu from './components/menu'
export default {
  name: "app",
  components:{
    'Menu':menu
  }
};
</script>



