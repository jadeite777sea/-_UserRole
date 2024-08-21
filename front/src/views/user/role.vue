<template>
  <div class="dashboard-container">

    <el-form label="搜索内容" label-width="80px" inline="true" size="small">
      <el-form-item label="搜索内容">
        <el-input
          v-model="tableData.searchContent"
          placeholder="请输入"
          clearable=""
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getRoleList">搜索</el-button>
        <el-button icon="el-icon-plus" size="mini" @click="handleCreateRole">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData.allData" @sort-change="handleSortChange">
      <el-table-column type="index" width="60" />
      <el-table-column prop="name" label="角色名称" sortable="custom" />
      <el-table-column prop="description" label="角色描述" sortable="custom" />
      <el-table-column prop="createTime" label="创建时间" sortable="custom" />
      <el-table-column prop="updateTime" label="更新时间" sortable="custom" />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" style="color: red;" @click="handleDelete([scope.row.id])">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page.sync="tableData.currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size.sync="tableData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.total"
      @size-change="getRoleList"
      @current-change="getRoleList"
    />

    <el-dialog
      class="user-edit-dialog"
      :title="roleEditForm.id ? '角色编辑' : '新建角色'"
      :visible.sync="roleEditDialogVisible"
      width="50%"
      top="8vh"
    >
      <el-form
        ref="roleEditForm"
        status-icon
        :model="roleEditForm"
        label-width="80px"
        rules="roleEditFormRules"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleEditForm.name" />
        </el-form-item>

        <el-form-item label="角色描述" prop="description">
          <el-input v-model="roleEditForm.description" />
        </el-form-item>

        <el-form-item>
          <el-button @click="roleEditDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addOrUpdateRole">确 定</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>

  </div>
</template>

<script>

import * as RoleApi from '@/api/role'

export default {
  name: 'Role',
  data() {
    return {

      tableData: {
        searchContent: '',
        list: [
          {
            id: 1,
            name: '张三',
            description: '法外狂徒',
            createTime: '',
            updateTime: ''

          }
        ],
        currentPage: 1,
        pageSize: 10,
        total: 40,
        allData: []

      },

      roleEditForm: {
        id: '',
        name: '',
        description: ''
      },
      roleEditFormRules: {
        name: [
          { required: true, trigger: 'blur', validator: this.validateName }
        ]
      },
      roleEditDialogVisible: false

    }
  },
  mounted() {
    this.getRoleList()
  },
  methods: {
    handleSortChange() {
      // 处理排序改变的操作，通常用于表格排序的响应
    },
    /**
     * 新增/编辑角色(后端请求)
     */
    addOrUpdateRole() {
      this.$refs.roleEditForm.validate(valid => {
        if (valid) {
          const tempApi = this.roleEditForm.id ? RoleApi.updateRole : RoleApi.addRole
          tempApi(this.roleEditForm).then(res => {
            this.$message.success('操作成功')
            this.getRoleList()
            this.roleEditDialogVisible = false
          })
        }
      })
    },
    /**
     * 获取角色列表
     */
    getRoleList() {
      RoleApi.getRoles(this.tableData.searchContent).then(res => {
        this.tableData.allData = res.data.data
        this.tableData.total = res.data.data.length
        this.startPagination()
      })
    },
    /**
     * 新增角色
     */
    handleCreateRole() {
      for (const key in this.roleEditForm) {
        this.roleEditForm[key] = ''
      }
      this.openRoleEditDialog()
    },
    openRoleEditDialog() {
      this.roleEditDialogVisible = true
      this.$nextTick(() => {
        this.$refs.roleEditForm.clearValidate()
      })
    },

    /**
     * 编辑角色
     * @param {Object} row
     */
    handleEdit(row) {
      for (const key in this.roleEditForm) {
        this.roleEditForm[key] = row[key]
      }
      this.openRoleEditDialog()
    },

    /**
     * 前端分页
     */
    startPagination() {
      // 角色数量较少，所以采用前端分页
      const start = (this.tableData.currentPage - 1) * this.tableData.pageSize
      const end = this.tableData.currentPage * this.tableData.pageSize
      this.tableData.list = this.tableData.allData.slice(start, end)
    },
    /**
     * 删除角色
     * @param {Number} id
     */
    handleDelete(id) {
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        RoleApi.deleteRoles([id]).then(() => {
          this.$message.success('删除成功')
          this.getRoleList()
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    }

  }
}

</script>
