/**
 * @param {string} path
 * @returns {Boolean}
 */
 export function isExternal(path) {
    return /^(https?:|mailto:|tel:)/.test(path)
  }
  
  /**
   * @param {string} str
   * @returns {Boolean}
   */
  export function validLoginUsername(str) {
    return str !== ''
  }
  
  /**
   * @param {string} str
   * @returns {Boolean}
   */
  export function validLoginPassword(str) {
    return str.length >= 6
  }
  
  /**
   * @param {string} str
   * @returns {Boolean}
   */
  export function validRegisterUsername(str) {
    const reg = /^[\u4E00-\u9FA5a-zA-Z0-9_-]{2,10}$/
    return reg.test(str) && str !== undefined
  }
  
  /**
   * @param {string} str
   * @returns {Boolean}
   */
  export function validRegisterPassword(str) {
    // const reg = /^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/
    // return reg.test(str)
    return true
  }
  
  /**
   * @param {string} email
   * @returns {Boolean}
   */
  export function validEmail(email) {
    const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    return reg.test(email)
  }
  