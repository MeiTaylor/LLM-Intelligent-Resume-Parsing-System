<template>
  <div class="background">
    <ElIcon style="font-size: 20px " :class="[icon, 'sub-el-icon']" />
    <span slot="title" style="font-size: 16px">{{title}}</span>
  </div>
</template>

<script>
  import { ElIcon } from 'element-plus'
  import { h } from 'vue'
  export default {
    name: "MenuItem",
    // functional: true,
    props: {
      icon: {
        type: String,
        default: ""
      },
      title: {
        type: String,
        default: ""
      }
    },
    components: { ElIcon }
  }
</script>

<style scoped>
  .background {
    /* background-color: #f8faf9; */
    width: 100%;
    height: 100%;
  }

  /* 这是菜单里面小的样式 */
  .background:hover {
    /* background-color: #fa7475; */
    /* 鼠标悬浮时的背景颜色 */
  }

  :deep(.sub-el-icon) {
    color: currentColor;
    width: 1em;
    height: 1em;
  }
</style>