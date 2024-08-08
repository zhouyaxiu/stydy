/**
 * @param {string} code 通过地区code获取地区层级
 * @returns {Boolean}
 */
export function getLevel (code) {
  let level = '-1'
  if (!code) {
    level = '-1'
  }
  if (code.substr(2, 12) === '0000000000') { // 省
    level = '0'
  } else if (code.substr(4, 12) === '00000000') { // 市
    level = '1'
  } else if (code.substr(6, 12) === '000000') { // 县
    level = '2'
  } else if (code.substr(8, 12) === '0000') { // 乡
    level = '3'
  } else if (code.substr(10, 12) === '00') { // 接种点
    level = '4'
  } else {
    level = '5'
  }
  return level
}
