<template>
  <div class="dashboard-container">
    <el-select
      v-model="roleForm.currentRoleName"
      filterable
      placeholder="请选择"
      remote
      :remote-method="query => { roleForm.searchName = query; getRoles() }"
      @change="handleCurrentRoleChange"
    >
      <el-option
        v-for="item in roleForm.roleList"
        :key="item.id"
        :label="item.name"
        :value="item.name"
      />
    </el-select>

    <!-- 重置、保存按钮 -->
    <el-button type="primary" style="margin-left: 50px" @click="resetPermission">重置</el-button>
    <el-button type="primary" @click="savePermission">保存</el-button>

    <!-- 间距调整 -->
    <div style="height: 20px" />

    <el-table
      :data="permissionTree"
      style="margin-bottom: 20px;"
      row-key="id"
      border
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <!-- 权限名称列 -->
      <el-table-column label="权限名称" width="300">
        <template slot-scope="scope">
          <el-checkbox
            v-model="scope.row.checked"
            :indeterminate="scope.row.indeterminate"
            style="margin-left: 5px; margin-right: 10px;"
            @change="handleCheckChange(scope.row)"
          />
          <!-- 如果 classify 是 'platform' 则加粗显示 -->
          <span v-if="scope.row.classify === 'platform'" style="font-weight: bold;">
            {{ scope.row.name }}
          </span>
          <span v-else>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <!-- 权限代码列 -->
      <el-table-column label="权限代码" prop="code" width="200" />

      <!-- 描述列 -->
      <el-table-column label="描述" prop="description" width="300" />

      <!-- 创建时间列 -->
      <el-table-column label="创建时间" prop="createTime" width="200" />
    </el-table>

  </div>
</template>

<script>

export default {
  data() {
    return {
      roleForm: {
        searchName: '', // 用于角色搜索的关键词
        roleList: [], // 角色列表
        currentRoleName: '', // 当前选中的角色名称
        currentRole: {}, // 当前选中的角色对象
        loading: false // 加载状态，用于显示加载动画等
      },
      roleList: [], // 所有角色的列表
      permissionTree: [{
        id: 1,
        name: '系统管理',
        code: 'SYS_MANAGE',
        description: '系统管理权限',
        createTime: '2024-08-18',
        checked: false,
        indeterminate: false,
        classify: 'platform',
        children: [
          {
            id: 2,
            name: '用户管理',
            code: 'USER_MANAGE',
            description: '用户管理权限',
            createTime: '2024-08-18',
            checked: false,
            indeterminate: false,
            classify: '',
            children: [
              {
                id: 3,
                name: '新增用户',
                code: 'ADD_USER',
                description: '新增用户权限',
                createTime: '2024-08-18',
                checked: false,
                indeterminate: false,
                classify: ''
              },
              {
                id: 4,
                name: '删除用户',
                code: 'DELETE_USER',
                description: '删除用户权限',
                createTime: '2024-08-18',
                checked: false,
                indeterminate: false,
                classify: ''
              }
            ]
          },
          {
            id: 5,
            name: '角色管理',
            code: 'ROLE_MANAGE',
            description: '角色管理权限',
            createTime: '2024-08-18',
            checked: false,
            indeterminate: false,
            classify: ''
          }
        ]
      },
      {
        id: 6,
        name: '内容管理',
        code: 'CONTENT_MANAGE',
        description: '内容管理权限',
        createTime: '2024-08-18',
        checked: false,
        indeterminate: false,
        classify: 'platform',
        children: [
          {
            id: 7,
            name: '文章管理',
            code: 'ARTICLE_MANAGE',
            description: '文章管理权限',
            createTime: '2024-08-18',
            checked: false,
            indeterminate: false,
            classify: ''
          }
        ]
      }], // 权限树数据，用于展示权限的树形结构
      permissionIdList: [], // 用于保存选中权限的 ID 列表
      platformMap: { // 平台映射，展示平台与管理平台的对应关系
        1: '展示平台',
        2: '管理平台'
      }
    }
  },
  mounted() {
    this.getRoles()
  },
  methods: {
    /**
     * 获取角色列表
     */
    getRoles() {
      // 这里可以添加获取角色列表的逻辑
    }
  }

}

</script>
