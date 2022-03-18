import Vue from 'vue'
import VueRouter from 'vue-router'
import About from '../views/About.vue'
import Management from '../views/Management.vue'
// import Login from '../views/Login.vue'
import store from '../store' // your vuex store 

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    beforeEnter: (to, from, next) => {
      if (store.getters.getUser.role=="admin") 
        next('/management')
      else 
        next('/dashboard')
    }  
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import(/* webpackChunkName: "about" */ '../views/Dashboard.vue'),
    beforeEnter: (to, from, next) => {
      if (store.getters.getUser.role=="admin") 
        next('/')
      else 
        next()
    }
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },
  {
    path: '/management',
    name: 'Management',
    component: Management,
    beforeEnter: (to, from, next) => {
      if (store.getters.getUser.role=="admin") 
        next()
      else 
        next('/')
    }
  },
  /*{
    path: '/login',
    name: 'Login',
    component: Login
  },*/
]

const router = new VueRouter({
  routes
})

export default router
