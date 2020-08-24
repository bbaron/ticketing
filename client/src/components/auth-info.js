export const AUTH_INFO_HEADER = "x-auth-info";

export const setAuthInfo = (authInfo) => {
  localStorage.setItem(AUTH_INFO_HEADER, authInfo);
};

export const getAuthInfo = () => localStorage.getItem(AUTH_INFO_HEADER);

export const removeAuthInfo = () => localStorage.removeItem(AUTH_INFO_HEADER);
