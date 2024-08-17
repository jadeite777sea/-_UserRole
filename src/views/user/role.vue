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
        <el-button icon="el-icon-plus" size="mini" @click="resetQuery">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData.list" @selection-change="val => tableData.selection = val" @sort-change="handleSortChange">
      <el-table-column type="index" width="60" />
      <el-table-column type="selection" width="50" />
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
      :current-page.sync="tableData.pageNum"
      :page-sizes="[10, 20, 30, 40]"
      :page-size.sync="tableData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.total"
      @size-change="getUserList"
      @current-change="getUserList"
    />

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
            description: '张三',
            createTime: '张三',
            updateTime: ''

          }
        ],
        selection: '',
        pageNum: 1,
        pageSize: 10,
        total: 40

      }

    }
  }
}

</script>
