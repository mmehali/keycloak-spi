import Vue from 'vue'
import Router from 'vue-router'
import editAccount from '@/components/editAccount'
import editPassword from '@/components/editPassword'
import mygroup from '@/components/mygroup'
import inviteUser from '@/components/inviteUser'

Vue.use(Router)

export default new Router({
  mode: 'history',
  linkActiveClass: "active",
  linkExactActiveClass: 'is-active',
  routes: [
    {
      path: '/',
      name: 'editAccount',
      component: editAccount
    },
    {
      path: '/editPassword',
      name: 'editPassword',
      component: editPassword
    },
    {
      path: '/mygroup',
      name: 'mygroup',
      component : mygroup
    },
    {
      path: '/inviteuser',
      name: 'inviteUser',
      component: inviteUser
    }
  ]
})