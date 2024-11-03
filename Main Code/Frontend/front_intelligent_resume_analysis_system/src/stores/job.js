// stores/jobDetailStore.js
import { defineStore } from 'pinia';

export const useJobDetail = defineStore('jobDetailStore', {
    state: () => ({
        clickJobMatching: '',
    }),
    actions: {
        setclickJobMatching(newName) {
            this.clickJobMatching = newName;
        },
    },
});
