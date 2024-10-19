<template>
  <div :class="{'has-logo':showLogo}">
    <logo :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        :unique-opened="false"
        active-text-color="#409EFF"
        :collapse-transition="false"
        mode="vertical"
      >
      <!--  -->
        <sidebar-item v-for="route in this.permission_routes.routes" :key="route.path" :item="route" :base-path="route.path"  />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'
import { mapState, mapWritableState } from 'pinia'
import Logo from './Logo.vue'
import SidebarItem from './SidebarItem.vue'

import variables from '@/styles/variables.scss?inline'
import { useSidebar } from '../../../stores/app'
import { useSetting } from '../../../stores/setting'
import { usePermissions } from '../../../stores/permission'

export default {
  name: 'sidebar',
  components: { SidebarItem, Logo },
  mounted(){
    console.log(this.permission_routes.routes)
  },
  computed: {
    // ...mapGetters([
    //   'permission_routes',
    //   'sidebar'
    // ]),
    ...mapState(useSidebar, {
      sidebar: store => store.sidebar
    }),
    ...mapState(usePermissions, {
      permission_routes: 'routes'
    }),
    // routes() {
    //   return this.$router.options.routes
    // },
    activeMenu() {
      const route = this.$route
      // console.log(this.permission_routes.routes)
      const { meta, path } = route
      console.log(path)
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      const setting = useSetting()
      return setting.sidebarLogo
      // return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>

<style>
  /* .el-menu{
    background-color: #304156;
    
  } */
</style>
