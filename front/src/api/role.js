import request from '@/utils/request'

export function getRoutes() {
  return request({
    url: '/vue-element-admin/routes',
    method: 'get'
  })
}

/**
 * 获取角色列表
 * @param {string} searchContent
 * @returns roles
 */
export function getRoles(searchContent) {
  return request.get('user-api/roles', { params: { searchContent }})
}

/**
 * 添加角色
 * @param {Object} data {name, description}
 */
export function addRole(data) {
  return request.post('user-api/roles', data)
}

/**
 * 更新角色
 * @param {Object} data {id, name, description}
 */
export function updateRole(data) {
  return request.put('user-api/roles', data)
}

/**
 * 删除角色
 * @param {list} roleIds
 */
export function deleteRoles(roleIds) {
  const url = 'user-api/roles?ids=' + roleIds.join(',')
  return request.delete(url)
}

/**
 * 获取所有权限
 */
export function getAllPermissions() {
  const url = 'user-api/permissions'
  return request.get(url)
}

/**
 * 获取角色的权限
 * @param {number} roleId
 */
export function getPermissionOfRole(roleId) {
  const url = 'user-api/roles/' + roleId + '/permissions'
  return request.get(url)
}

