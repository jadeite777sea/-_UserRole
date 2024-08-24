import request from '@/utils/request'

/**
 * 获取用户自身信息
 */
export function getInfo() {
  return request.get('user-api/users/me')
}

/**
 * 获取用户列表
 * @param {Object} data {userName, minCreateTime, maxCreateTime, orderBy, orderMethod, pageNum, pageSize}
 * @returns users
 */
export function getUsers(data) {
  const params = new URLSearchParams(data)
  // orderBy 的驼峰转换为下划线
  if (params.has('orderBy')) {
    params.set('orderBy', params.get('orderBy').replace(/([A-Z])/g, '_$1').toLowerCase())
  }
  const url = `user-api/users?${params.toString()}`
  return request.get(url)
}

/**
 * 添加用户
 * @param {Object} data {userName, trueName, password, email, phone, gender, address, introduction, roleIds}
 */
export function addUser(data) {
  return request.post('user-api/users', data)
}

/**
 * 更新用户信息
 * @param {Object} data {id, userName, trueName, email, phone, gender, address, introduction, roleIds}
 */
export function updateUser(data) {
  return request.put('user-api/users', data)
}

/**
 * 删除用户
 * @param {list} userIds
 */
export function deleteUsers(userIds) {
  const url = 'user-api/users?ids=' + userIds.join(',')
  return request.delete(url)
}

/**
 * 修改用户状态
 * @param {number} userId
 * @param {number} status
 */
export function changeUserStatus(userId, status) {
  const url = `user-api/users/${userId}/status?status=${status}`
  return request.put(url)
}
