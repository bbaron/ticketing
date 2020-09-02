export const AUTH_INFO_HEADER = "x-auth-info";
export const setAuthInfo = (authInfo) => {
  sessionStorage.setItem(AUTH_INFO_HEADER, authInfo);
};

export const getAuthInfo = () => sessionStorage.getItem(AUTH_INFO_HEADER);

export const removeAuthInfo = () => sessionStorage.removeItem(AUTH_INFO_HEADER);
