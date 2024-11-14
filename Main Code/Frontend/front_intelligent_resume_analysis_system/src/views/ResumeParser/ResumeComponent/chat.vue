<template>
    <div class="chat-container">
        <div class="chat-box">
            <div v-for="(message, index) in messages" :key="index" :class="['chat-message', message.role]">
                <span>{{ message.text }}</span>
            </div>
        </div>
        <div class="input-box">
            <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type a message" :disabled="isSending" />

            <button @click="sendMessage" :disabled="isSending">
                Send
            </button>
        </div>
    </div>
</template>

<script setup>
    import { ref, reactive, defineProps } from 'vue';
    import axios from 'axios';
    const messages = reactive([{
        role: 'ai',
        text: '我是你的贴心AI助手小历，有什么可以帮助你的吗？'
    }]);

    const newMessage = ref('')
    const isSending = ref(false)
    const sendMessage = () => {
        console.log(`output->props.RID`, props.RID)
        if (newMessage.value.trim() !== '') {
            // Add user message
            messages.push({ role: 'user', text: newMessage.value });
            isSending.value = true;
            // Simulate AI response
            axios.post('http://localhost:8080/api/conversation/ask', { question: newMessage.value, resumeId: props.RID })
                .then(response => {
                    console.log(response);
                    messages.push({ role: 'ai', text: response.data });
                })
                .catch(error => {
                    console.error(error);
                })
                .finally(() => {
                    isSending.value = false;
                });

            newMessage.value = '';

        }
    }

    const props = defineProps({
        RID: String
    })

</script>

<style>
    .chat-container {
        display: flex;
        flex-direction: column;
        height: 100vh;
        max-width: 900px;
        margin: auto;
        justify-content: center;
        align-items: center;
    }

    .chat-box {
        background-color: rgb(238, 240, 243);
        width: 100%;
        flex: 1;
        overflow-y: auto;
        padding: 10px;
        border: 1px solid #ccc;
        display: flex;
        flex-direction: column;
        border-radius: 10px;
        /* Adjust this value to change the roundness of the corners */
    }

    .input-box {
        display: flex;
        width: 100%;
        border-top: 1px solid #ccc;
        padding: 10px;
    }


    .chat-message {
        margin: 5px 0;
        padding: 10px;
        border-radius: 5px;
        max-width: 70%;
        word-wrap: break-word;
        display: inline-block;
        width: fit-content;
    }

    .chat-message.ai {
        background-color: #fff;
        text-align: left;
        align-self: flex-start;
    }

    .chat-message.user {
        background-color: #007bff;
        color: white;
        text-align: right;
        align-self: flex-end;
    }


    input {
        flex: 1;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        margin-right: 10px;
    }

    input:disabled {
        background-color: #f0f0f0;
        cursor: not-allowed;

    }

    button {
        padding: 10px 20px;
        border: none;
        background-color: #007bff;
        color: white;
        border-radius: 5px;
        cursor: pointer;
    }

    button:hover {
        background-color: #0056b3;
    }

    button:disabled {
        background-color: #a0cfff;
        cursor: not-allowed;
    }


    @keyframes spin {
        0% {
            transform: rotate(0deg);
        }

        100% {
            transform: rotate(360deg);
        }
    }
</style>