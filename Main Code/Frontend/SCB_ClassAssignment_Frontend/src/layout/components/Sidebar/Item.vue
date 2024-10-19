<template>
  <ElIcon style="font-size: 20px" :class="[icon, 'sub-el-icon']" />
  <span slot="title" style="font-size: 16px">{{title}}</span>
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

<style>
:deep(.sub-el-icon) {
  color: currentColor;
  width: 1em;
  height: 1em;
}
</style>

