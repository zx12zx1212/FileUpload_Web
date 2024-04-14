<template>
    <a-result :status="statusText" :title="titleText" :sub-title="subtitleText">
        <template #extra>
            <a-button type="primary" :icon="h(CloudUploadOutlined)" @click="reUpload">上傳檔案</a-button>
            <router-link to="/home">
                <a-button :icon="h(RollbackOutlined)">回首頁</a-button>
            </router-link>
        </template>
        <div v-show="statusText === 'success'">
            <p style="font-size: 16px"><strong>上傳資訊</strong></p>
            <p>上傳檔案：{{ fileList }}</p>
            <p>上傳人員：{{ empList }}</p>
            <p>是否可以修改檔案：{{ modifyFile }}</p>
            <p>是否可以修改權限：{{ modifyPermissions }}</p>
            <p>是否可以刪除檔案：{{ deleteFile }}</p>
        </div>
    </a-result>
</template>

<script lang="ts" setup>
import { h, defineEmits } from 'vue';
import { defineProps } from 'vue';
import { RollbackOutlined, CloudUploadOutlined } from '@ant-design/icons-vue';

const emit = defineEmits(['onReupload']);

defineProps({
    statusText: { type: String, },
    titleText: { type: String, },
    subtitleText: { type: String, },
    fileList: { type: Array, },
    empList: { type: Array, },
    modifyFile: { type: String },
    modifyPermissions: { type: String },
    deleteFile: { type: String }
})
const reUpload = () => {
    emit('funBack', true, false)
}
</script>