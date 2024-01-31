import {goto} from "$app/navigation";
import {isLogin} from "$lib/login_stores.js";
import {onMount} from "svelte";
const apiBaseUrl = import.meta.env.VITE_CORE_API_BASE_URL;
/**
 * @param {string} msg
 */
export function checkLogout(msg) {
    onMount(() => {
        if (localStorage.getItem("nickname") != undefined) {
            alert(msg);
            goto("/");
        }
    })
}

/**
 * @param {string} msg
 */
export function checkLogin(msg) {
    onMount(() => {
        if (localStorage.getItem("nickname")) {
            alert(msg);
            goto("/");
        }
    })
}

export async function logout() {
    await fetch(`${apiBaseUrl}/member/logout`, {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'POST',
        credentials: 'include'
    });
    localStorage.removeItem("username");
    localStorage.removeItem("nickname");
    isLogin.set(false);
    await goto('/');
}