<template>
    <div v-show="isUploadData" style="width: 90%;margin:0px auto;margin-top: 5%;">
        <a-divider orientation="left">上傳檔案</a-divider>
        <a-row :gutter="[16, 16]">
            <a-col :span="12">
                <a-upload-dragger :file-list="fileList" :multiple="true" :before-upload="beforeUpload"
                    @remove="handleRemove">
                    <p class="ant-upload-drag-icon" style="margin-top: 4%;">
                        <inbox-outlined></inbox-outlined>
                    </p>
                    <p class="ant-upload-text">點選或拖曳檔案至此區域即可上傳</p>
                </a-upload-dragger>
            </a-col>
            <a-col :span="12" style="text-align: left;">
                <a-row :gutter="[8, 16]">
                    <a-col :span="24">
                        請選擇要分享該檔案的組室：
                        <a-select v-model:value="depValue" mode="multiple" style="width: 100%" placeholder="請選擇"
                            :options="depList" @change="handleChange"></a-select>
                    </a-col>
                    <a-col :span="24">
                        請選擇要分享該檔案的人員：
                        <a-select v-model:value="empValue" mode="multiple" style="width: 100%" placeholder="請選擇"
                            :options="empList" :disabled="depValue.length === 0"></a-select>
                    </a-col>
                    <a-col :span="24">
                        請勾選：
                        <a-checkbox v-model:checked="modifyValue">修改檔案</a-checkbox>
                        <a-checkbox v-model:checked="permissionsValue">修改權限</a-checkbox>
                        <a-checkbox v-model:checked="deleteValue">刪除檔案</a-checkbox>
                    </a-col>
                    <a-col :sm="12" :md="12" :lg="12">
                        <div style="text-align:right;">
                            <a-button type="primary" :disabled="empValue.length === 0" :loading="uploading"
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
    <div>
        <UploadResult :statusText="status" :titleText="title" :subtitleText="subtitle" v-show="isUploadStatus"
            @funBack="reUpload" :fileList="fileResult" :empList="empResult" :modifyFile="modifyFile"
            :modifyPermissions="modifyPermissions" :deleteFile="deleteFile" />
    </div>

</template>
<script lang="ts" setup>
import axios from 'axios';
import { h, ref } from 'vue';
// import { useRouter } from 'vue-router';
import { RollbackOutlined, UploadOutlined, InboxOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import type { SelectProps, UploadProps } from 'ant-design-vue';
import { useUserStore } from '../store/user'
import UploadResult from '../components/UploadResult.vue'

// const router = useRouter()
const userStore = useUserStore()
const empId = userStore.loginUserId
const fileList = ref<UploadProps['fileList']>([])
const uploading = ref<boolean>(false)
// 選中的所有部門值
const depValue = ref([])
// 選中的所有員工值
const empValue = ref([])
// 是否能修改、權限、刪除
const modifyValue = ref<boolean>(false);
const permissionsValue = ref<boolean>(false);
const deleteValue = ref<boolean>(false);
// 部門員工(接api)
const allDepList = ref([])
// 所有部門
const depList = ref<SelectProps['options']>([])
// 所有部門員工
const empList = ref<SelectProps['options']>([])
// 上傳狀態
const status = ref<string>('')
const title = ref<string>('')
const subtitle = ref<string>('')
const isUploadStatus = ref<boolean>(false);
const isUploadData = ref<boolean>(true);
const fileResult = ref([])
const empResult = ref([])
const modifyFile = ref<string>('')
const modifyPermissions = ref<string>('')
const deleteFile = ref<string>('')


// 載入組室
axios.get('http://localhost:8181/getAllDepartment')
    .then(res => {
        const tmp = res.data
        depList.value = res.data.map(item => ({ value: item.departmentId, label: item.departmentName }))
        tmp.forEach(item => {
            item.employeeListDto.forEach(employee => {
                allDepList.value.push({
                    'depId': item.departmentId,
                    'depName': item.departmentName,
                    'empId': employee.id,
                    'empName': employee.name
                })
            })
        })
    })

// 載入組室人員
const handleChange = (value: string[]) => {
    // 選中的組室人員 and 移除目前登入使用者
    empList.value = []
    value.forEach(dep => {
        allDepList.value.forEach(emp => {
            if (dep === emp.depId && !(emp.empId === empId)) {
                empList.value.push(
                    { value: emp.empId, label: `${emp.depName} - ${emp.empName}`, depId: dep })
            }
        })
    })
    // 移除選中的組室人員
    const tmp = ref([])
    empValue.value.forEach(emp => {
        allDepList.value.forEach(total_emp => {
            if (depValue.value.includes(total_emp.depId) && total_emp.empId === emp) {
                tmp.value.push(emp)
            }
        })
    })
    empValue.value = tmp.value
};

// 上傳檔案
const beforeUpload: UploadProps['beforeUpload'] = file => {
    fileList.value = [...(fileList.value || []), file];
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
    const recodeFile = []
    fileList.value.forEach((file: UploadProps['fileList'][number]) => {
        formData.append('files', file)
        recodeFile.push(file.name)
    });
    formData.append('sharer', empValue.value.toString())
    formData.append('creator', empId)
    formData.append('modifyFile', modifyValue.value.toString());
    formData.append('modifyPermission', permissionsValue.value.toString());
    formData.append('deletePermission', deleteValue.value.toString());
    axios.post('http://localhost:8181/uploadFiles', formData)
        .then(() => {
            fileList.value = [];
            uploading.value = false;
            message.success('上傳成功！');
            status.value = 'success'
            title.value = '上傳成功!'
            subtitle.value = ''
            fileResult.value = recodeFile
            empList.value.forEach(list => {
                empValue.value.forEach(emp => {
                    if (emp === list.value) {
                        empResult.value.push(list.label)
                    }
                })
            })
            modifyFile.value = modifyValue.value ? "是" : "否"
            modifyPermissions.value = permissionsValue.value ? "是" : "否"
            deleteFile.value = deleteValue.value ? "是" : "否"
        })
        .catch(() => {
            uploading.value = false;
            message.error('上傳成功失敗！');
            status.value = 'error'
            title.value = '上傳失敗！'
            subtitle.value = '請再重新上傳檔案.'
        }).finally(() => {
            isUploadStatus.value = true
            isUploadData.value = false
        })
}

const reUpload = (value1, value2) => {
    isUploadData.value = value1
    isUploadStatus.value = value2
}
</script>
