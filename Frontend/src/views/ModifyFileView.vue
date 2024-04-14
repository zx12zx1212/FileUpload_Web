<template>
    <div style="width: 90%;margin:0px auto;margin-top: 5%;">
        <a-divider orientation="left">修改檔案</a-divider>
        <a-row :gutter="[16, 16]">
            <a-col :sm="24" :md="12">
                <a-row :gutter="[16, 16]">
                    <a-col :sm="24" :md="24" :lg="24" :xl="12">
                        <a-card title="目前檔案" style="margin: 0px auto;">
                            {{ fileName }}
                            <a-button type="link" :icon="h(FileMarkdownOutlined)"
                                @click="downLoadFile(route.params.fileId, fileName)" />
                        </a-card>
                    </a-col>

                    <a-col :sm="24" :md="24" :lg="24" :xl="12">
                        <a-upload-dragger :file-list="fileList" :before-upload="beforeUpload" @remove="handleRemove">
                            <p class="ant-upload-drag-icon" style="margin-top: 4%;">
                                <inbox-outlined></inbox-outlined>
                            </p>
                            <p class="ant-upload-text">點選或拖曳檔案至此區域即可上傳</p>
                        </a-upload-dragger>
                    </a-col>
                </a-row>
            </a-col>
            <a-col :sm="24" :md="12" style="text-align: left;">
                <a-row :gutter="[8, 16]">
                    <a-col :span="24">
                        請選擇要分享該檔案的組室：
                        <a-select v-model:value="depValue" mode="multiple" style="width: 100%" placeholder="請選擇"
                            :options="depList" :disabled=true></a-select>
                    </a-col>
                    <a-col :span="24">
                        請選擇要分享該檔案的人員：
                        <a-select v-model:value="empValue" mode="multiple" style="width: 100%" placeholder="請選擇"
                            :options="empList" :disabled=true></a-select>
                    </a-col>
                    <a-col :span="24">
                        目前擁有的權限：
                        <a-checkbox v-model:checked="modifyValue" :disabled=true>修改檔案</a-checkbox>
                        <a-checkbox v-model:checked="permissionsValue" :disabled=true>修改權限</a-checkbox>
                        <a-checkbox v-model:checked="deleteValue" :disabled=true>刪除檔案</a-checkbox>
                    </a-col>
                    <a-col :sm="12" :md="12" :lg="12">
                        <div style="text-align:right;">
                            <a-button type="primary" :disabled="!(fileList.length >= 1)" :loading="uploading"
                                @click="submitUpload" :icon="h(UploadOutlined)">
                                {{ uploading ? '上傳中' : '開始上傳' }}
                            </a-button>
                        </div>
                    </a-col>
                    <a-col :sm="12" :md="12" :lg="12">
                        <div style="text-align:left;">
                            <router-link to="/home">
                                <a-button :icon="h(RollbackOutlined)">回首頁</a-button>
                            </router-link>
                        </div>
                    </a-col>
                </a-row>
            </a-col>
        </a-row>
    </div>
</template>

<script setup lang="ts">
import { h, ref } from 'vue'
import { RollbackOutlined, FileMarkdownOutlined, UploadOutlined } from '@ant-design/icons-vue';
import type { SelectProps, UploadProps } from 'ant-design-vue';
import { message } from 'ant-design-vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios'
import { useUserStore } from '../store/user'

//  
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const fileList = ref<UploadProps['fileList']>([])
const uploading = ref<boolean>(false)
//檔案名稱
const fileName = ref('')
// 選中的所有部門值
const depValue = ref([])
// 選中的所有員工值
const empValue = ref([])
// 所有部門
const depList = ref<SelectProps['options']>([])
// 所有部門員工
const empList = ref<SelectProps['options']>([])
// 是否能修改、權限、刪除
const modifyValue = ref<boolean>(false);
const permissionsValue = ref<boolean>(false);
const deleteValue = ref<boolean>(false);
const empId = userStore.loginUserId

axios.post(`http://localhost:8181/getFileAllFPermission/${route.params.fileId}`)
    .then(res => {
        res.data.forEach(item => {
            if (!depValue.value.includes(item.sharerDepId)) {
                depValue.value.push(item.sharerDepId)
                depList.value.push({ value: item.sharerDepId, label: item.sharerDepName })
            }
            if (!empValue.value.includes(item.sharer)) {
                empValue.value.push(item.sharer)
                empList.value.push({ value: item.sharer, label: `${item.sharerDepName} - ${item.sharerName}` })
            }
            if (item.sharer === empId) {
                modifyValue.value = item.modifyFile
                permissionsValue.value = item.modifyPermission
                deleteValue.value = item.deletePermission
            }
        })

    }).then(() => {
        axios.get(`http://localhost:8181/getFileName/${route.params.fileId}`)
            .then(res => {
                fileName.value = res.data
            })
    })

// 下載檔案
const downLoadFile = (event, file_name) => {
    axios.get(`http://localhost:8181/downloadFile/${event}`, {
        responseType: 'blob',
    }).then(res => {
        const url = window.URL.createObjectURL(new Blob([res.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', file_name);
        document.body.appendChild(link);
        link.click();
    })
}

// 上傳檔案
const beforeUpload: UploadProps['beforeUpload'] = file => {
    fileList.value = [file];
    return false
};

const handleRemove: UploadProps['onRemove'] = file => {
    const index = fileList.value.indexOf(file);
    const newFileList = fileList.value.slice();
    newFileList.splice(index, 1);
    fileList.value = newFileList;
};

const submitUpload = () => {
    const formData = new FormData();
    fileList.value.forEach((file: UploadProps['fileList'][number]) => {
        formData.append('files', file)
    });
    formData.append('fileNumber', route.params.fileId.toString())
    axios.put('http://localhost:8181/modifyFile', formData)
        .then(() => {
            fileList.value = [];
            uploading.value = false;
            message.success('上傳成功！');
            router.push('/home')
        })
        .catch(() => {
            uploading.value = false;
            message.error('上傳成功失敗！');
            router.push('/home')
        });
}


</script>