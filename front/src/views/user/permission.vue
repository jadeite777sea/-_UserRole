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

import * as RoleApi from '@/api/role'

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
    this.getAllPermission()
  },
  methods: {
    /**
     * 获取角色列表
     */
    getRoles() {
      this.roleForm.loading = true
      RoleApi.getRoles(this.roleForm.searchName).then((res) => {
        this.roleForm.roleList = res.data.data
        this.roleForm.total = res.data.data.length
        // 从页面的params中获取roleId
        const roleId = this.$route.params.roleId
        if (roleId) {
          this.getPermissionOfRole(roleId)
          this.roleForm.currentRole = this.roleForm.roleList.find(item => item.id === roleId)
          this.roleForm.currentRoleName = this.roleForm.currentRole.name
          this.$route.params.roleId = undefined
        }
      }).finally(() => {
        this.roleForm.loading = false
      })
    },
    /**
     * 根据列表构建树
     * @param {Array} list 列表
     * @param {String} idField id字段名
     * @param {String} pidField 父id字段名
     */
    buildTreeByList(list, idField = 'id', pidField = 'parentId') {
      const listData = Array.from(list)
      const tree = []
      const idTreeMap = {}
      // 第一个循环找到所有根节点
      for (let i = 0; i < listData.length; i++) {
        const item = listData[i]
        // 判断是否是一个根节点
        if (!item[pidField] || listData.findIndex(it => it[idField] === item[pidField]) === -1) {
          tree.push(item)
          idTreeMap[item[idField]] = item // 子节点id号 : 父节点
        }
      }
      // 将所有根节点从listData中进行删除
      for (let i = 0; i < listData.length; i++) {
        if (idTreeMap[listData[i][idField]]) {
          listData.splice(i, 1)
          i--
        }
      }
      // 为什么 +1 ？？？
      let oldLength = listData.length + 1
      // 自顶向下构建树
      while (listData.length > 0) {
        for (let i = 0; i < listData.length; i++) {
          const item = listData[i]
          const parent = idTreeMap[item[pidField]]
          if (parent) {
            if (!parent.children) {
              parent.children = []
            }
            parent.children.push(item)
            idTreeMap[item[idField]] = item
            listData.splice(i, 1)
            i--
          }
        }
        if (oldLength === listData.length) {
          break
        }
        oldLength = listData.length
      }
      return tree
    },
    /**
     * 遍历树
     * @param {Array} tree 树
     * @param {Function} callback 回调
     */
    traverseTree(tree, callback) {
      for (let i = 0; i < tree.length; i++) {
        const item = tree[i]
        callback(item)
        if (item.children) {
          this.traverseTree(item.children, callback)
        }
      }
    },
    /**
     * 递归勾选/取消勾选所有子节点
     * @param {Object} node 节点
     * @param {Boolean} checked 是否勾选
     */
    checkAllChildren(node, checked) {
      if (node.children) {
        node.children.forEach(item => {
          item.checked = checked
          this.checkAllChildren(item, checked)
        })
      }
    },
    /**
     * 处理勾选状态改变
     * @param {Object} row 行数据
     */
    handleCheckChange(row) {
      // 将所有与当前节点permissionCode相同的节点勾选/取消勾选
      if (row.code) {
        this.traverseTree(this.permissionTree, item => {
          if (item.code === row.code) {
            item.checked = row.checked
          }
        })
      }
      this.checkAllChildren(row, row.checked)
      this.permissionTree.forEach(item => {
        this.modifyNodeCheckStatus(item)
      })
    },
    /**
     * 修改权限树的勾选状态
     * @param {Array} permissionList 权限列表
     */
    changePermissionCheckStatus(permissionList) {
      // 判断根节点是否被勾选
      this.traverseTree(this.permissionTree, item => {
        if (permissionList.includes(item.id)) {
          item.checked = true
        } else {
          item.checked = false
        }
      })
      this.permissionTree.forEach(item => {
        this.modifyNodeCheckStatus(item)
      })
    },
    /**
     * 修改节点的勾选状态
     * @param {Object} node 节点
     */
    modifyNodeCheckStatus(node) {
      // 若根节点被勾选则其孩子节点都需要被勾选
      // 递归勾选
      const children = node.children
      if (children) {
        node.children.forEach(item => {
          this.modifyNodeCheckStatus(item)
        })
        node.checked = node.check || children.some(item => item.checked)
        node.indeterminate = children.some(item => item.indeterminate) || children.some(item => item.checked) && children.some(item => !item.checked)
      }
    },
    /**
     * 重置权限
     */
    resetPermission() {
      this.changePermissionCheckStatus(this.permissionIdList)
    },
    /**
     * 获取权限列表
     * @returns {Array} 权限列表
     */
    getPermissionListOfTree() {
      const permissionList = []
      this.traverseTree(this.permissionTree, item => {
        if (item.checked && item.classify !== 'platform') {
          permissionList.push(item.id)
        }
      })
      return permissionList
    },
    /**
     * 处理当前角色改变
     */
    handleCurrentRoleChange() {
      this.roleForm.currentRole = this.roleForm.roleList.find(item => item.name === this.roleForm.currentRoleName)
      this.getPermissionOfRole(this.roleForm.currentRole.id)
    },
    /**
     * 获取角色的权限
     * @param {Number} roleId 角色id
     */
    getPermissionOfRole(roleId) {
      RoleApi.getPermissionOfRole(roleId).then(res => {
        const permissionList = res.data.data
        this.permissionIdList = permissionList.map(item => item.id)
        this.changePermissionCheckStatus(this.permissionIdList)
      })
    },

    /**
     * 保存权限
     */
    savePermission() {
      if (!this.roleForm.currentRole || !this.roleForm.currentRole.id) {
        this.$message({
          message: '请选择角色',
          type: 'warning'
        })
        return
      }
    },

    /**
     * 获取所有权限
     */
    getAllPermission() {
      RoleApi.getAllPermissions().then(res => {
        const data = res.data.data
        data.forEach(item => {
          item.checked = false
          item.indeterminate = false
        })
        const platforms = [...new Set(data.map(item => item.platform))]
        this.permissionTree = []
        // 构造树列表
        for (let i = 0; i < platforms.length; i++) {
          const platform = platforms[i]
          const platformData = data.filter(item => item.platform === platform)
          this.permissionTree.push({ id: -1 - i, name: this.platformMap[platform], classify: 'platform', children: this.buildTreeByList(platformData) })
        }
        if (this.permissionIdList.length > 0) {
          this.changePermissionCheckStatus(this.permissionIdList)
        }
        console.log(this.permissionTree)
      })
    }

  }

}

</script>
