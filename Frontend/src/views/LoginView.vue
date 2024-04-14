<template>
    <div style="width: 90%;margin:0px auto;margin-top: 5%;">
        <a-form :model="formState" :label-col="{ span: 8 }" :wrapper-col="{ span: 8 }">
            <a-form-item label="帳號" name="username" :rules="[{ required: true, message: '請輸入帳號!' }]">
                <a-input v-model:value="formState.username" />
            </a-form-item>

            <a-form-item label="密碼" name="password" :rules="[{ required: true, message: '請輸入密碼!' }]">
                <a-input-password v-model:value="formState.password" />
            </a-form-item>

            <a-form-item :wrapper-col="{ offset: 8, span: 8 }">
                <a-button type="primary" @click="loginSubmit">登入</a-button>
            </a-form-item>
        </a-form>
    </div>
</template>
<script lang="ts" setup>
import axios from 'axios';
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/user'
import { message } from 'ant-design-vue';

interface FormState {
    username: string;
    password: string;
}

const formState = reactive<FormState>({
    username: '',
    password: '',
});

const router = useRouter()
const userStore = useUserStore()

const loginSubmit = () => {
    let formData = new FormData();
    formData.append('username', formState.username)
    formData.append('password', formState.password)
    axios.post('http://localhost:8181/login', formData)
        .then(res => {
            if (res.status === 200) {
                userStore.loginStatus = true
                userStore.loginUserId = res.data.empId
                userStore.loginUserName = res.data.empName
                console.log(userStore.loginStatus, userStore.loginUserId, userStore.loginUserName)
                router.push('/')
            }
            if (res.status === 202 && res.data === "帳號或密碼錯誤！") {
                message.error("帳號或密碼錯誤！請再重新登入")
            }
        })
}
</script>