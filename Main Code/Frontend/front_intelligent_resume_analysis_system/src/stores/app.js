import Cookies from 'js-cookie'
import { defineStore } from "pinia";

export const useSidebar = defineStore(
    'sidebar',
    {
        state: () => ({
            sidebar: {
                opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
                withoutAnimation: false
            },
            device: 'desktop'
        }),
        getters: {

        },
        actions: {
            TOGGLE_SIDEBAR() {
                this.sidebar.opened = !this.sidebar.opened
                this.sidebar.withoutAnimation = false
                if (this.sidebar.opened) {
                    Cookies.set('sidebarStatus', 1)
                } else {
                    Cookies.set('sidebarStatus', 0)
                }
            },
            CLOSE_SIDEBAR(withoutAnimation) {
                Cookies.set('sidebarStatus', 0)
                this.sidebar.opened = false
                this.sidebar.withoutAnimation = withoutAnimation
            },
            TOGGLE_DEVICE(device) {
                this.device = device
            },
            toggleSideBar() {
                // commit('TOGGLE_SIDEBAR')
                this.TOGGLE_SIDEBAR()
            },
            closeSideBar(withoutAnimation) {
                // commit('CLOSE_SIDEBAR', withoutAnimation)
                this.CLOSE_SIDEBAR(withoutAnimation)
            },
            toggleDevice(device) {
                // commit('TOGGLE_DEVICE', device)
                this.TOGGLE_DEVICE(device)
            }
        },
    },
)



