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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getUserList">搜索</el-button>
        <el-button icon="el-icon-plus" size="mini" @click="handleCreateRole">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData.list" @sort-change="handleSortChange">
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
      @size-change="getUserList"
      @current-change="getUserList"
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
          <el-button type="primary" @click="addOrUpdateUser">确 定</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>

  </div>
</template>

<script>

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
        total: 40

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
  methods: {
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
    }

  }
}

</script>
