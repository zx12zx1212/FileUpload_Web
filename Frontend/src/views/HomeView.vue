<template>
  <div style="width: 90%;margin:0px auto;margin-top: 5%;">
    <a-row :gutter="[16, 16]">
      <a-col :sm="15" :md="17" :lg="18" :xl="19">
        <div style="text-align: left;margin-left: 10px;">
          <a-avatar style="background-color: #87d068">
            <template #icon>
              <UserOutlined />
            </template>
          </a-avatar>
          您好，{{ empName }}
        </div>
      </a-col>
      <a-col :sm="5" :md="4" :lg="3" :xl="3">
        <router-link to="/upload">
          <a-button type="primary" :icon="h(CloudUploadOutlined)">上傳檔案</a-button>
        </router-link>
      </a-col>
      <a-col :sm="1" :md="1" :lg="2" :xl="1">
        <a-button :icon="h(LogoutOutlined)" @click="logout">登出</a-button>
      </a-col>
      <a-col :span="24">
        <a-table :columns="columns" :data-source="fileTableContent" :pagination="paginationConfig" bordered>
          <template #customFilterDropdown="{ setSelectedKeys, selectedKeys, confirm, clearFilters, column }">
            <div style="padding: 8px">
              <a-input ref="searchInput" :placeholder="`搜尋${column.title}`" :value="selectedKeys[0]"
                style="width: 188px; margin-bottom: 8px; display: block"
                @change="e => setSelectedKeys(e.target.value ? [e.target.value] : [])"
                @pressEnter="handleSearch(selectedKeys, confirm, column.dataIndex)" />
              <a-button type="primary" size="small" style="width: 90px; margin-right: 8px"
                @click="handleSearch(selectedKeys, confirm, column.dataIndex)">
                <template #icon>
                  <SearchOutlined />
                </template>
                搜尋
              </a-button>
              <a-button size="small" style="width: 90px" @click="handleReset(clearFilters)">
                重置
              </a-button>
            </div>
          </template>

          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'fileName'">
              {{ record.fileName }}
              <a-button type="link" :icon="h(FileMarkdownOutlined)"
                @click="downLoadFile(record.fileId, record.fileName)" />
            </template>

            <template v-else-if="column.key === 'sharer'">
              {{ record.sharer }}
              <a-button type="link" :icon="h(MailOutlined)" :href="`mailto:${record.sharerEmail}`" />
            </template>

            <template v-else-if="column.key === 'modifyFile'">
              <a-button :disabled="!record.modifyFile" @click="modifyFile(record)">修改檔案</a-button>
            </template>

            <template v-else-if="column.key === 'modifyPermission'">
              <a-button :disabled="!record.modifyPermission" @click="modifyPermission(record)">修改權限</a-button>
            </template>

            <template v-else-if="column.key === 'deletePermission'">
              <a-button :disabled="!record.deletePermission" danger @click="deleteFile(record.fileId)">刪除</a-button>
            </template>
          </template>
        </a-table>
      </a-col>
    </a-row>
  </div>

</template>
<script lang="ts" setup>
import { h } from 'vue';
import axios from 'axios'
import { reactive, ref } from 'vue';
import { LogoutOutlined, CloudUploadOutlined, SearchOutlined, FileMarkdownOutlined, MailOutlined, UserOutlined } from '@ant-design/icons-vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()
const searchInput = ref()
const fileTableContent = ref()
const state = reactive({
  searchText: '',
  searchedColumn: '',
})
const paginationConfig = reactive({
  defaultPageSize: 3,
  total: 0,
  pageSizeOptions: ['3', '5', '10', '15', '20'],
  showSizeChanger: true
})
const empId = userStore.loginUserId
const empName = userStore.loginUserName


axios.get(`http://localhost:8181/getFiles/${empId}`)
  .then(res => {
    fileTableContent.value = res.data
    paginationConfig.total = fileTableContent.value.length
  })


// 資料表標題
const columns = [
  {
    title: '項目',
    dataIndex: 'items',
    key: 'items',
    align: 'center',
    sorter: (a, b) => a.items - b.items,
  },
  {
    title: '檔名',
    dataIndex: 'fileName',
    key: 'fileName',
    align: 'center',
    customFilterDropdown: true,
    onFilter: (value, record) => record.fileName.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  {
    title: '上傳日期',
    dataIndex: 'createDate',
    key: 'createDate',
    align: 'center',
    sorter: (a, b) => a.create_date - b.create_date,
  },
  {
    title: '上傳人員',
    dataIndex: 'sharer',
    key: 'sharer',
    align: 'center',
  },
  {
    title: '修改檔案',
    dataIndex: 'modifyFile',
    key: 'modifyFile',
    align: 'center',
  },
  {
    title: '修改權限',
    dataIndex: 'modifyPermission',
    key: 'modifyPermission',
    align: 'center',
  },
  {
    title: '刪除檔案',
    dataIndex: 'deletePermission',
    key: 'deletePermission',
    align: 'center',
  }
];

// 搜尋檔案
const handleSearch = (selectedKeys, confirm, dataIndex) => {
  confirm();
  state.searchText = selectedKeys[0];
  state.searchedColumn = dataIndex;
};

const handleReset = clearFilters => {
  clearFilters({ confirm: true });
  state.searchText = '';
};

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
// 修改檔案
const modifyFile = (event) => {
  router.push({
    name: 'modifyFile',
    params: {
      fileId: event.fileId,
    }
  })
}
// 編輯權限
const modifyPermission = (event) => {
  router.push({
    name: 'modifyPermission',
    params: {
      fileId: event.fileId,
    }
  })
}

// 刪除檔案
const deleteFile = (event) => {
  axios.delete(`http://localhost:8181/deleteFile/${event}`)
  window.location.reload();
}

const logout = () => {
  userStore.loginStatus = false
  userStore.loginUserId = ''
  userStore.loginUserName = ''
  router.push('/login')

}
</script>
