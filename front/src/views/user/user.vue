<template>
  <div class="dashboard-container">
    <el-form ref="form" :model="tableData" label-width="80px" :inline="true" size="small">
      <el-form-item label="用户名称">
        <el-input v-model="tableData.userName" aria-placeholder="请输入用户名称" />
      </el-form-item>

      <el-form-item label="创建时间">
        <el-date-picker v-model="tableData.minCreatetime" class="data-picker" type="datetime" placeholder="起始时间" style="width: 200px;" />
        <el-time-picker v-model="tableData.maxCreatetime" class="data-picker" type="datetime" placeholder="截止时间" style="width: 200px;" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getUserList">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div>
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleCreateUser">新增</el-button>
      <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleBatchDelete">删除</el-button>
      <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImportUser">导入</el-button>
    </div>

    <el-table :data="tableData.list" @selection-change="val => tableData.selection = val" @sort-change="handleSortChange">
      <el-table-column type="index" label="索引" width="80" />
      <el-table-column type="selection" width="80" />
      <el-table-column width="80">
        <template slot-scope="scope">
          <!-- Uncomment to display user avatar -->
          <!-- <img :src="scope.row.avatar" class="table-avatar" /> -->
          <img :id="'avatar-' + scope.row.id" class="table-avatar">
        </template>
      </el-table-column>
      <el-table-column prop="userName" label="用户名" sortable="custom" />
      <el-table-column prop="trueName" label="真实姓名" sortable="custom" />
      <el-table-column prop="roleList" label="角色" sortable="custom" />
      <el-table-column prop="createTime" label="创建时间" sortable="custom" />
      <el-table-column prop="status" label="是否激活" sortable="custom" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="handleSwitch(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" style="color: red;" @click="handleDelete([scope.row.id])">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page.sync="tableData.pageNum"
      :page-sizes="[10, 20, 30, 40]"
      :page-size.sync="tableData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.total"
      @size-change="getUserList"
      @current-change="getUserList"
    />

    <el-dialog
      class="user-edit-dialog"
      :title="userEditForm.id ? '用户编辑' : '新建用户'"
      :visible.sync="userEditDialogVisible"
      width="50%"
      top="8vh"
    >
      <el-form
        ref="userEditForm"
        status-icon
        :model="userEditForm"
        label-width="80px"
        :rules="userEditForm.id ? userUpdateRules : userCreateRules"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userEditForm.userName" />
        </el-form-item>

        <el-form-item label="真实姓名" prop="trueName">
          <el-input v-model="userEditForm.trueName" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="userEditForm.password" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userEditForm.email" />
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="userEditForm.gender">
            <el-radio label="0">男</el-radio>
            <el-radio label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="地址">
          <el-input v-model="userEditForm.address" />
        </el-form-item>

        <el-form-item label="简介">
          <el-input v-model="userEditForm.introduction" />
        </el-form-item>

        <el-form-item label="电话">
          <el-input v-model="userEditForm.phone" />
        </el-form-item>

        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="userEditForm.roleIds" multiple placeholder="请选择角色">
            <el-option v-for="role in allRoles" :key="role.id" :label="role.name" :value="role.id" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button @click="userEditDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addOrUpdateUser">确 定</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>

    <el-dialog title="导入用户" :visible.sync="importDialogVisible" width="60%">
      <div>
        <el-button type="primary" @click="downloadTemplate">下载示例文件</el-button>
        <el-button type="primary" @click="uploadExcel">点击上传excel</el-button>
      </div>

      <el-table :data="userData" style="width: 100%; margin-top: 20px;">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="序号" width="100" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="realname" label="真实姓名" />
        <el-table-column prop="password" label="密码" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="gender" label="性别" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="bio" label="简介" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="role" label="角色" />
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="importAllUsers">导入全部</el-button>
        <el-button type="primary" @click="importSelectedUsers">导入已选择用户</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

import { getRoles } from '@/api/role'
import LoadingUtils from '@/utils/loading-utils'
import * as UserApi from '@/api/user'
import md5 from 'js-md5'

const copyObject = obj => JSON.parse(JSON.stringify(obj))
export default {
  name: 'User',
  data() {
    return {

      tableData: {
        userName: '',
        maxCreatetime: '',
        minCreatetime: '',
        list: [],
        selection: '',
        pageNum: 1,
        pageSize: 10,
        total: 40

      },
      userEditForm: {
        id: '',
        userName: '',
        trueName: '',
        password: '',
        email: '',
        gender: '',
        address: '',
        introduction: '',
        phone: '',
        roleIds: []
      },
      userEditDialogVisible: false,
      importDialogVisible: false,
      allRoles: [],
      userCreateRules: {
        userName: [{ required: true, trigger: 'blur', validator: this.userNameValidator }],
        password: [{ required: true, trigger: 'change', validator: this.passwordValidator }],
        roleIds: [{ required: true, trigger: 'change', validator: this.roleValidator }]
      },
      userUpdateRules: {
        userName: [{ required: true, trigger: 'blur', validator: this.userNameValidator }],
        password: [{ trigger: 'change', validator: this.passwordValidator }],
        roleIds: [{ required: true, trigger: 'change', validator: this.roleValidator }]
      },

      userData: [{
        id: 1,
        username: 'john_doe',
        realname: 'John Doe',
        password: 'password123',
        email: 'john@example.com',
        gender: 'Male',
        address: '123 Main St, Springfield, IL',
        bio: 'Software developer with 5 years of experience.',
        phone: '123-456-7890',
        role: 'Admin'
      },
      {
        id: 2,
        username: 'jane_smith',
        realname: 'Jane Smith',
        password: 'securePass456',
        email: 'jane@example.com',
        gender: 'Female',
        address: '456 Oak St, Springfield, IL',
        bio: 'Graphic designer and digital artist.',
        phone: '987-654-3210',
        role: 'User'
      },
      {
        id: 3,
        username: 'michael_brown',
        realname: 'Michael Brown',
        password: 'myPass789',
        email: 'michael@example.com',
        gender: 'Male',
        address: '789 Pine St, Springfield, IL',
        bio: 'Marketing specialist with a passion for data analysis.',
        phone: '555-123-4567',
        role: 'Editor'
      }],

      currentEditRow: null

    }
  },
  computed: {

  },
  mounted() {
    this.getUserList()
    this.getAllRoles()
  },

  methods: {
    /**
     * 创建用户
     */
    handleCreateUser() {
      this.resetUserEditForm()
      this.openUserEditForm()
    },

    /**
     * 重置查询条件
     */
    resetQuery() {
      this.tableData.userName = ''
      this.tableData.minCreateTime = ''
      this.tableData.maxCreateTime = ''
    },
    /**
     * 重置用户编辑表单
     */
    resetUserEditForm() {
      for (const key in this.userEditForm) {
        this.userEditForm[key] = ''
      }
      this.userEditForm.roleIds = []
    },

    /**
     * 获取所有角色
     */
    getAllRoles() {
      getRoles().then(res => {
        this.allRoles = res.data.data
        // this.$refs.userImportDialog.setRoles(this.allRoles)
      })
    },
    userNameValidator(rule, value, callback) {
      if (!value) {
        callback(new Error('请输入用户名'))
      } else if (this.userEditForm.id && value === this.currentEditRow.userName) {
        callback()
      } else {
        callback()
        // 使用接口判断用户名是否为空
        // checkUserName(value).then(res => {
        //   callback(res.data.data ? new Error('用户名已存在') : undefined)
        // })
      }
    },

    passwordValidator(rule, value, callback) {
      if (!value && this.userEditForm.id) {
        callback()
      } else if (!value || value.length < 6) {
        callback(new Error('密码长度不能小于6位'))
      } else {
        callback()
      }
    },

    roleValidator(rule, value, callback) {
      if (!value || value.length === 0) {
        callback(new Error('角色不能为空'))
      } else {
        callback()
      }
    },
    getUserList() {
      UserApi.getUsers(this.tableData).then(res => {
        this.tableData.list = res.data.data.content
        this.tableData.total = res.data.data.totalElements
        // 更新头像
        // this.$nextTick(() => {
        //   this.tableData.list.forEach(row => {
        //     this.getAvatar(row.id, row)
        //   })
        // })
      })
    },
    /**
     * 切换用户状态
     * @param {object} row 行数据
     */
    handleSwitch(row) {
      UserApi.changeUserStatus(row.id, row.status).then(() => {
        this.$message.success('操作成功')
      })
    },

    // 编辑
    handleEdit(row) {
      this.currentEditRow = row
      for (const key in this.userEditForm) {
        this.userEditForm[key] = row[key]
      }
      this.userEditForm.roleIds = row.roleList
        ? row.roleList.map(item => {
          const role = this.allRoles.find(role => role.name === item)
          return role && role.id
        })
        : []
      this.userEditForm.roleIds.filter(id => id)
      this.openUserEditForm()

      // this.userEditDialogVisible = true
    },
    /**
     * 打开用户编辑窗口
     */
    openUserEditForm() {
      this.userEditDialogVisible = true
      this.$nextTick(() => {
        this.$refs.userEditForm.clearValidate()
      })
    },

    handleDelete(userIds) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        LoadingUtils.createFullScreenLoading('正在删除...')
        UserApi.deleteUsers(userIds).then(() => {
          this.$message.success('删除成功')
          this.getUserList()
        }).finally(() => {
          LoadingUtils.closeFullScreenLoading()
        })
      })
    },
    handleBatchDelete() {
      if (this.tableData.selection.length === 0) {
        this.$message.warning('请选择要删除的用户')
        return
      }
      const userIds = this.tableData.selection.map(item => item.id)
      this.handleDelete(userIds)
    },

    handleSortChange({ column, prop, order }) {
      // 处理排序改变的操作，通常用于表格排序的响应
      this.tableData.orderBy = prop
      this.tableData.orderMethod = order === 'ascending' ? 'asc' : 'desc'
      this.getUserList()
    },
    handleImportUser() {
      this.importDialogVisible = true
    },
    downloadTemplate() {
      // 处理下载示例文件逻辑
    },
    uploadExcel() {
      // 处理上传Excel文件逻辑
    },
    importAllUsers() {
      // 处理导入全部用户的逻辑
    },
    importSelectedUsers() {
      // 处理导入已选择用户的逻辑
    },
    /**
     * 添加或更新用户
     */
    addOrUpdateUser() {
      this.$refs.userEditForm.validate(valid => {
        if (valid) {
          const params = copyObject(this.userEditForm)
          if (!params.password) {
            delete params.password
          } else {
            params.password = md5(params.password)
          }
          LoadingUtils.createFullScreenLoading('正在保存...')
          console.log(params)
          const tempApi = this.userEditForm.id ? UserApi.updateUser : UserApi.addUser
          tempApi(params).then(res => {
            this.$message.success('操作成功')
            if (!this.userEditForm.id) {
              this.userEditForm.id = res.data.data.id
            }
            // 这是用来更新头像
            // this.updateAvatar()
            this.getUserList()
          }).finally(() => {
            this.userEditDialogVisible = false
            LoadingUtils.closeFullScreenLoading()
          })
        }
      })
    }

  }
}

</script>

<style scoped>

.data-picker {
  margin-right: 10px;
  width: 160px;
}

</style>
