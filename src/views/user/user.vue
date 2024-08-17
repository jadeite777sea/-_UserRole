<template>
  <div class="dashboard-container">
    <el-form ref="form" :model="tableData" label-width="80px" :inline="true" size="small">
      <el-form-item label="用户名称">
        <el-input v-model="tableData.usernName" aria-placeholder="请输入用户名称" />
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
        rules="userEditForm.id ? userUpdateRules : userCreateRules"
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

        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action=""
            :auto-upload="false"
            :show-file-list="false"
            @change="file => handleAvatarChange(file)"
          >
            <!--            <img v-if="avatarUploadData.url" :src="avatarUploadData.url" class="avatar">&lt;!&ndash; 需要把变量声明完成&ndash;&gt;-->
            <!--            <i v-else class="el-icon-plus avatar-uploader-icon" />-->
          </el-upload>
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

import avatarUploadData from 'mockjs'

export default {
  name: 'User',
  data() {
    return {

      tableData: {
        usernName: '',
        maxCreatetime: '',
        minCreatetime: '',
        list: [
          {
            id: 1,
            userName: '张三',
            trueName: '张三',
            roleList: [],
            createTime: '',
            status: true

          }
        ],
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
      }]

    }
  },
  computed: {

  },

  methods: {
    avatarUploadData,
    getUserList() {
      // 获取用户列表的方法，通常会调用API来获取用户数据
    },

    handleCreateUser() {
      // 处理创建用户的操作，通常是打开一个对话框让用户输入信息
      this.userEditDialogVisible = true
    },

    // 切换用户性别
    handleSwitch() {
      // 处理用户性别切换的逻辑
    },

    // 编辑
    handleEdit(row) {
      for (const key in this.userEditForm) {
        this.userEditForm[key] = row[key]
        this.userEditDialogVisible = true
      }
    },

    handleDelete(userIds) {
      // 处理删除用户的操作，参数row通常是用户的数据对象
      this.$confirm('此操作将永久删除该用户，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 对接删除用户接口
      })
    },
    handleBatchDelete() {
      // 处理批量删除用户的操作
      // 获取选中的用户ID数组
      // 如果没有选中任何用户，提示用户选择
      if (this.tableData.selection === '') {
        this.$message({
          type: 'warning',
          message: '请选择要删除的用户'
        })
        return
      }
      const userIds = this.tableData.selection.map(item => item.id)

      // 调用单个删除方法进行批量删除
      this.$confirm('此操作将永久删除选中的用户，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 循环删除选中的用户
        userIds.forEach(userId => {
          this.handleDelete(userId)
        })

        // 在这里可以添加其他操作，例如刷新表格数据
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

    resetQuery() {
      // 重置查询条件，通常用于重置表单或查询参数
    },

    handleSortChange() {
      // 处理排序改变的操作，通常用于表格排序的响应
    },
    addOrUpdateUser() {

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
