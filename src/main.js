import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Keycloak from 'keycloak-js'
import VeeValidate from 'vee-validate'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUser, faEnvelope, faCheck, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
library.add(faUser, faEnvelope, faCheck, faExclamationTriangle)
Vue.component('font-awesome-icon', FontAwesomeIcon)
require("./assets/main.scss")

Vue.use(VeeValidate)

var keycloak = Keycloak({
  url: 'http://localhost:8080/auth',
  realm: 'test',
  clientId: 'inotest'
});

keycloak.init({ onLoad: 'login-required' }).error(function() {
  alert('failed to initialize');
});
export {keycloak};
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')




