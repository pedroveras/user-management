export default class Utils {
  static cleanAuth(){
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('userProfile');
    localStorage.removeItem('userProfile');
  }

}
