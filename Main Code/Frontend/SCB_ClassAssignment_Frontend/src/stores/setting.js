import { defineStore } from "pinia";
import defaultSettings from '../setting'

const { showSettings, fixedHeader, sidebarLogo } = defaultSettings

export const useSetting = defineStore('setting', {
    state: () => ({
        showSettings: showSettings,
        fixedHeader: fixedHeader,
        sidebarLogo: sidebarLogo
    }),
    actions: {
        CHANGE_SETTING({ key, value }) {
            // eslint-disable-next-line no-prototype-builtins
            if (this.hasOwnProperty(key)) {
                this.$state[key] = value
            }
        },
        changeSetting(data) {
            this.CHANGE_SETTING(data)
        }
    },
})