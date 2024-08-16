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
        :model="userEditForm"
        label-width="80px"
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
            <img v-if="avatarUploadData.url" :src="avatarUploadData.url" class="avatar"><!-- 需要把变量声明完成-->
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button @click="userEditDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addOrUpdateUser">确 定</el-button>
        </el-form-item>

      </el-form>
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
      allRoles: [],
      dialogVisible: false

    }
  },
  computed: {

  },

  methods: {
    avatarUploadData,

    handleCreateUser() {
      this.userEditDialogVisible = true
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
