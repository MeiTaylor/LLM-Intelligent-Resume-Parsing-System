<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <router-view :key="key"/>
    </transition>
    <el-tooltip placement="top" content="Back to top">
      <back-to-top :custom-style="myBackToTopStyle" :visibility-height="300" :back-position="50"
                   transition-name="fade"/>
    </el-tooltip>
  </section>
</template>

<script>
  import BackToTop from '@/components/BackToTop/index.vue'

  export default {
    name: 'AppMain',
    components: { BackToTop },
    data() {
      return {
        // customizable button style, show/hide critical point, return position
        myBackToTopStyle: {
          right: '50px',
          bottom: '50px',
          width: '40px',
          height: '40px',
          'border-radius': '4px',
          'line-height': '45px', // 请保持与高度一致以垂直居中
          background: '#e7eaf1' // 按钮的背景颜色
        }
      }
    },
    computed: {
      key() {
        console.log(this.$route.path)
        return this.$route.path
      }
    }
  }
</script>

<style scoped>
  .app-main {
    /*50 = navbar  */
    min-height: calc(90vh - 50px);
    width: 100%;
    position: relative;
    overflow: hidden;
  }

.fixed-header + .app-main {
    padding-top: 50px;
  }
</style>

<style lang="scss">
  // fix css style bug in open el-dialog
  .el-popup-parent--hidden {
    .fixed-header {
      padding-right: 15px;
    }
  }
</style>
