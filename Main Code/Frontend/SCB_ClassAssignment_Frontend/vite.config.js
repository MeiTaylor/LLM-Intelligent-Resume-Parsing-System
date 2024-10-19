import { fileURLToPath, URL } from 'node:url'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import path from 'path'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(),vueJsx({
    // options are passed on to @vue/babel-plugin-jsx
  }),
  createSvgIconsPlugin({
    // 指定需要缓存的图标文件夹
    iconDirs: [path.resolve(process.cwd(), 'src/icons')],
    // 指定symbolId格式
    symbolId: 'icon-[dir]-[name]',

    /**
     * 自定义插入位置
     * @default: body-last
     */
    // inject?: 'body-last' | 'body-first'

    /**
     * custom dom id
     * @default: __svg__icons__dom__
     */
    // customDomId: '__svg__icons__dom__',
  }),],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // esbuild: {
  //   "loader": {
  //     '.js': 'jsx',
  //   },
  // },
  // optimizeDeps: {
  //   esbuildOptions: {
  //     "loader": {
  //       '.js': 'jsx',
  //     },
  //   },
  //   include: [
  //     'path-to-regexp.js'
  //   ]
  // },

})
