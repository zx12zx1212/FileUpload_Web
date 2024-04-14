<template>
    <div style="width: 90%;margin:0px auto;margin-top: 5%;">
        <a-divider orientation="left">修改權限</a-divider>
        <a-row :gutter="[16, 16]">
            <a-col :sm="24" :md="12">
                <a-card title="目前檔案" style="margin: 0px auto;">
                    {{ fileName }}
                    <a-button type="link" :icon="h(FileMarkdownOutlined)"
                        @click="downloadFile(route.params.fileId, fileName)" />
                </a-card>
            </a-col>
            <a-col :sm="24" :md="12" style="text-align: left;">
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
                    <a-col :span="24" v-show="isCreator">
                        目前擁有的權限：
                        <a-checkbox v-model:checked="selfmodifyValue" disabled>修改檔案</a-checkbox>
                        <a-checkbox v-model:checked="selfpermissionsValue" disabled>修改權限</a-checkbox>
                        <a-checkbox v-model:checked="selfdeleteValue" disabled>刪除檔案</a-checkbox>
                    </a-col>
                    <a-col :span="24">
                        請勾選欲修改的選項：
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
</template>

<script setup lang="ts">
import { h, ref } from 'vue'
import { RollbackOutlined, FileMarkdownOutlined, UploadOutlined } from '@ant-design/icons-vue';
import type { SelectProps } from 'ant-design-vue';
import { message } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { useRoute } from 'vue-router';
import axios from 'axios'
import { useUserStore } from '../store/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
//  
const uploading = ref<boolean>(false)
//檔案名稱
const fileName = ref('')
// 選中的所有部門值
const depValue = ref([])
// 選中的所有員工值
const empValue = ref([])
// 是否能修改、權限、刪除
const modifyValue = ref<boolean>(false);
const permissionsValue = ref<boolean>(false);
const deleteValue = ref<boolean>(false);
const selfmodifyValue = ref<boolean>(false);
const selfpermissionsValue = ref<boolean>(false);
const selfdeleteValue = ref<boolean>(false);
const isCreator = ref<boolean>(false);
// 部門員工(接api)
const allDepList = ref([])
// 所有部門
const depList = ref<SelectProps['options']>([])
// 所有部門員工
const empList = ref<SelectProps['options']>([])
const empId = userStore.loginUserId

axios.post(`http://localhost:8181/getFileAllFPermission/${route.params.fileId}`)
    .then(res => {
        res.data.forEach(item => {
            if (!depValue.value.includes(item.sharerDepId)) {
                depValue.value.push(item.sharerDepId)
            }
            if (!empValue.value.includes(item.sharer)) {
                empValue.value.push(item.sharer)
            }
            if (item.creator === empId) {
                if (item.creator === item.sharer) {
                    isCreator.value = true
                    selfmodifyValue.value = item.modifyFile
                    selfpermissionsValue.value = item.modifyPermission
                    selfdeleteValue.value = item.deletePermission
                }
                else {
                    modifyValue.value = item.modifyFile
                    permissionsValue.value = item.modifyPermission
                    deleteValue.value = item.deletePermission
                }
            }
            if (item.sharer === empId && item.creator !== empId) {
                modifyValue.value = item.modifyFile
                permissionsValue.value = item.modifyPermission
                deleteValue.value = item.deletePermission
            }
        })
    }).then(() => {
        axios.get('http://localhost:8181/getAllDepartment')
            .then(res2 => {
                const tmp = res2.data
                depList.value = res2.data.map(item => ({ value: item.departmentId, label: item.departmentName }))
                tmp.forEach(item => {
                    item.employeeListDto.forEach(employee => {
                        allDepList.value.push({
                            'depId': item.departmentId,
                            'depName': item.departmentName,
                            'empId': employee.id,
                            'empName': employee.name
                        })
                        if (depValue.value.includes(item.departmentId)) {

                            if (employee.id === empId) {
                                empList.value.push({ value: employee.id, label: item.departmentName + ' - ' + employee.name, 'depId': item.departmentId, disabled: true })
                            } else {
                                empList.value.push({ value: employee.id, label: item.departmentName + ' - ' + employee.name, 'depId': item.departmentId, disabled: false })
                            }

                        }
                    })

                })
            })
    }).then(() => {
        axios.get(`http://localhost:8181/getFileName/${route.params.fileId}`)
            .then(res => {
                fileName.value = res.data
            })
    })
// 載入組室人員
const handleChange = (value: string[]) => {
    // 選中的組室人員 and 移除目前登入使用者
    empList.value = []
    value.forEach(dep => {
        allDepList.value.forEach(emp => {
            if (dep === emp.depId) {
                if (emp.empId === empId) {
                    empList.value.push(
                        { value: emp.empId, label: `${emp.depName} - ${emp.empName}`, 'depId': dep, disabled: true })
                } else {
                    empList.value.push(
                        { value: emp.empId, label: `${emp.depName} - ${emp.empName}`, 'depId': dep, disabled: false })
                }
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
// 下載檔案
const downloadFile = (event, file_name) => {
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
const submitUpload = () => {
    const formData = new FormData();
    formData.append("fileNumber", route.params.fileId)
    formData.append("sharer", empValue.value)
    formData.append("modifyFile", modifyValue.value)
    formData.append("modifyPermission", permissionsValue.value)
    formData.append("deletePermission", deleteValue.value)
    axios.put(`http://localhost:8181/modifyPermission`, formData)
        .then(() => {
            uploading.value = false;
            message.success('修改完成！');
            router.push('/home')
        })
}

</script>