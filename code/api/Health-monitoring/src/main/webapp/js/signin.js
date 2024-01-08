document.addEventListener('DOMContentLoaded', function () {
    async function generateRandomString(length) {
        const array = new Uint32Array(length);
        window.crypto.getRandomValues(array);
        return Array.from(array, (dec) => ("0" + dec.toString(16)).substr(-2)).join("");
    }

    async function sha256(plain) {
        const encoder = new TextEncoder();
        const data = encoder.encode(plain);
        return window.crypto.subtle.digest("SHA-256", data);
    }

    function base64urlencode(str) {
        return btoa(String.fromCharCode.apply(null, new Uint8Array(str)))
            .replace(/\+/g, "-")
            .replace(/\//g, "_")
            .replace(/=+$/, "");
    }

    async function pkceChallengeFromVerifier(v) {
        const hashed = await sha256(v);
        return base64urlencode(hashed);
    }

    function utf8_to_b64(str) {
        return window.btoa(unescape(encodeURIComponent(str)));
    }

    window.onload = start;

    async function start() {
        var state = await generateRandomString(28);
        var code_verifier = await generateRandomString(28);
        var code_challenge = await pkceChallengeFromVerifier(code_verifier);

        console.log("state:", state);
        console.log("code_verifier:", code_verifier);
        localStorage.setItem("codeverif", code_verifier);
        console.log("code_challenge:", code_challenge);

        var step = utf8_to_b64(state + "#" + code_challenge);
        console.log("step:", step);

        var step2 = "Bearer " + step;

        $.ajax({
            url: "http://localhost:8080/api/authorize",
            type: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
                "Pre-Authorization": step2,
            },
            complete: function (data) {
                console.log("Authorization request completed.");
                console.log("data.responseJSON:", data.responseJSON);

                // Scenario: Successful Authorization Request
                if (data.status === 302) {
                    console.log("Authorization request successful.");
                    localStorage.setItem("signInId", data.responseJSON.signInId);

                    document.getElementById("signInButton").onclick = function () {
                        var signInId = localStorage.getItem("signInId");
                        var mail = document.getElementById("username").value;
                        var password = document.getElementById("password").value;

                        let reqObj = { mail: mail, password: password, signInId: signInId };
                        console.log(JSON.stringify(reqObj));
                        console.log("Sending authentication request...");

                        // Simulating authentication request (Replace with actual API endpoint)
                        $.ajax({
                            url: "http://localhost:8080/api/authenticate/",
                            type: "POST",
                            data: JSON.stringify(reqObj),
                            dataType: "json",
                            headers: {
                                Accept: "application/json",
                                "Content-Type": "application/json",
                            },
                            success: function (data) {
                                console.log("Authentication success:", data);

                                // Scenario: Successful Authentication
                                var code_verifier = localStorage.getItem("codeverif");
                                localStorage.setItem("mail", mail);
                                var access = "Bearer " + utf8_to_b64(data.authCode + "#" + code_verifier);

                                // Simulating token request (Replace with actual API endpoint)
                                $.ajax({
                                    url: "http://localhost:8080/api/oauth/token",
                                    type: "GET",
                                    headers: {
                                        Accept: "application/json",
                                        "Content-Type": "application/json",
                                        "Post-Authorization": access,
                                    },
                                    success: function (data) {
                                        alert("Welcome Back!");
                                        console.log("Token request success:", data);

                                        localStorage.setItem("accesstoken", data.accessToken);
                                        localStorage.setItem("refreshtoken", data.refreshToken);
                                        localStorage.removeItem("signInId");
                                        console.log("ok");
                                        location.href = "home.html";
                                    },
                                    error: function (xhr, textStatus, errorThrown) {
                                        console.error("Error in getting token:", errorThrown);
                                        // Handle error cases if needed
                                    },
                                });
                            },
                            error: function (xhr, textStatus, errorThrown) {
                                // Scenario: Authentication Failure
                                console.error("Authentication failure:", errorThrown);
                                alert("Authentication failure. Please check your credentials and try again.");
                                // Handle error cases if needed

                                // Clear the form fields
                                document.getElementById("username").value = "";
                                document.getElementById("password").value = "";
                                // Handle error cases if needed
                            },
                        });
                    };
                } else {
                    alert("Authentication failure. Please check your credentials and try again.");
                    // Handle error cases if needed

                    // Clear the form fields
                    document.getElementById("username").value = "";
                    document.getElementById("password").value = "";
                }
            }
        });
    }
});