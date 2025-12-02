// =======================================
// CONFIGS DAS APIS
// =======================================
const API_USERS = "http://localhost:8080/users";
const API_LOGIN = "http://localhost:8080/users/login";
const API_FILME = "http://localhost:8080/movies/search";
const API_SUGGEST = "http://localhost:8080/movies/suggest";

// =======================================
// VARIÁVEIS GLOBAIS
// =======================================
let meuId = null; // ID do usuário logado

// =======================================
// USUÁRIOS
// =======================================

// Criar usuário
async function criarUsuario() {
    const user = document.getElementById("c_username");
    const email = document.getElementById("c_email");
    const senha = document.getElementById("c_senha");

    if (!user || !email || !senha) return;

    const username = user.value.trim();
    const mail = email.value.trim();
    const pass = senha.value.trim();

    if (!username || !mail || !pass) {
        alert("Preenche os campos direito, pô!");
        return;
    }

    try {
        const resp = await fetch(API_USERS, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, email: mail, password: pass })
        });

        if (!resp.ok) {
            alert("Erro ao criar usuário.");
            return;
        }

        alert("Usuário criado com sucesso!");
        user.value = "";
        email.value = "";
        senha.value = "";

    } catch (e) {
        console.error(e);
        alert("Erro na requisição.");
    }
}

// Listar todos os usuários
async function carregarUsuarios() {
    const lista = document.getElementById("listaUsers");
    if (!lista) return;

    lista.innerHTML = "<li>Carregando...</li>";

    try {
        const resp = await fetch(API_USERS);
        const data = await resp.json();

        if (!Array.isArray(data) || data.length === 0) {
            lista.innerHTML = "<li>Nenhum usuário encontrado.</li>";
            return;
        }

        lista.innerHTML = "";
        data.forEach(u => {
            const li = document.createElement("li");
            li.textContent = `${u.username} - ${u.email}`;
            lista.appendChild(li);
        });

    } catch (e) {
        console.error(e);
        alert("Erro ao carregar usuários.");
    }
}

// Login
async function logar() {
    const user = document.getElementById("l_username");
    const senha = document.getElementById("l_senha");

    if (!user || !senha) return;

    const username = user.value.trim();
    const pass = senha.value.trim();

    if (!username || !pass) {
        alert("Preenche tudo aí antes de logar.");
        return;
    }

    try {
        const resp = await fetch(API_LOGIN, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password: pass })
        });

        if (!resp.ok) {
            alert("Usuário ou senha inválidos.");
            return;
        }

        const json = await resp.json();
        meuId = json.id;

        alert("Login ok! Bem-vindo " + json.username);

        // Abre a tela de perfil
        abrirTela("telaPerfil");

        // Carrega o perfil completo
        carregarPerfil(meuId);

    } catch (e) {
        console.error(e);
        alert("Erro ao logar.");
    }
}

// =======================================
// PERFIL, SEGUIDORES E SEGUINDO
// =======================================

async function carregarPerfil(id) {
    // Preenche informações do usuário
    const respUser = await fetch(`${API_USERS}/${id}`);
    const user = await respUser.json();
    document.getElementById("infoUsuario").innerHTML = `
        <p><strong>Usuário:</strong> ${user.username}</p>
        <p><strong>Email:</strong> ${user.email}</p>
    `;

    // Lista quem ele segue
    const respSeguindo = await fetch(`${API_USERS}/${id}/following`);
    const seguindo = await respSeguindo.json();
    const listaSeguindo = document.getElementById("listaSeguindo");
    listaSeguindo.innerHTML = "";
    if (seguindo.length > 0) {
        seguindo.forEach(u => {
            const li = document.createElement("li");
            li.textContent = u.username;
            listaSeguindo.appendChild(li);
        });
    } else listaSeguindo.innerHTML = "<li>Você não segue ninguém ainda.</li>";

    // Lista seguidores
    const respSeguidores = await fetch(`${API_USERS}/${id}/followers`);
    const seguidores = await respSeguidores.json();
    const listaSeguidores = document.getElementById("listaSeguidores");
    listaSeguidores.innerHTML = "";
    if (seguidores.length > 0) {
        seguidores.forEach(u => {
            const li = document.createElement("li");
            li.textContent = u.username;
            listaSeguidores.appendChild(li);
        });
    } else listaSeguidores.innerHTML = "<li>Nenhum seguidor ainda.</li>";

    // Dropdown de usuários pra seguir
    carregarUsuariosParaSeguir(id);
}

// Carrega dropdown com usuários disponíveis pra seguir
async function carregarUsuariosParaSeguir(meuId) {
    const select = document.getElementById("selectUsuarios");
    select.innerHTML = "<option>Carregando...</option>";

    try {
        const resp = await fetch(API_USERS);
        const data = await resp.json();

        select.innerHTML = "";
        data.forEach(u => {
            if (u.id !== meuId) { // não mostra você mesmo
                const option = document.createElement("option");
                option.value = u.id;
                option.textContent = u.username;
                select.appendChild(option);
            }
        });

    } catch (e) {
        console.error(e);
        select.innerHTML = "<option>Erro ao carregar usuários</option>";
    }
}

// Seguir usuário
async function seguirUsuario() {
    const select = document.getElementById("selectUsuarios");
    const targetId = select.value;
    if (!targetId) {
        alert("Escolhe alguém pra seguir!");
        return;
    }

    try {
        const resp = await fetch(`${API_USERS}/${meuId}/follow?targetId=${targetId}`, {
            method: "POST"
        });

        if (!resp.ok) {
            alert("Erro ao seguir usuário.");
            return;
        }

        const json = await resp.json();
        alert("Agora você segue: " + json.followed.username);

        // Atualiza listas de seguindo e seguidores
        carregarPerfil(meuId);

    } catch (e) {
        console.error(e);
        alert("Erro na requisição.");
    }
}

// =======================================
// FILMES
// =======================================

// Buscar filme exato
// BUSCAR FILME EXATO (sem mostrar sugestões)
async function buscarFilme() {
    const titulo = document.getElementById("filtroTitulo");
    const lista = document.getElementById("listaFilmes");

    if (!titulo || !lista) return;

    const nome = titulo.value.trim();
    lista.innerHTML = "<li>Carregando...</li>";

    if (!nome) {
        alert("Digite o nome do filme!");
        lista.innerHTML = "";
        return;
    }

    try {
        const resp = await fetch(`${API_FILME}?titulo=${encodeURIComponent(nome)}`);
        if (!resp.ok) {
            lista.innerHTML = "<li>Nenhum filme encontrado.</li>";
            return;
        }

        const filme = await resp.json();

        lista.innerHTML = `
            <li class="filme-card">
                <img src="${filme.Poster}" alt="${filme.Title}">
                <div class="filme-info">
                    <h3>${filme.Title} (${filme.Year})</h3>
                    <p><strong>Gênero:</strong> ${filme.Genre}</p>
                    <p><strong>Diretor:</strong> ${filme.Director}</p>
                    <p><strong>Nota:</strong> ${filme.imdbRating}</p>
                    <p><strong>Sinopse:</strong> ${filme.Plot}</p>
                </div>
            </li>
        `;
    } catch (e) {
        console.error(e);
        lista.innerHTML = "<li>Erro ao buscar filme.</li>";
    }
}

// Sugestões só quando chamar explicitamente
async function sugerirFilmes() {
    const titulo = document.getElementById("filtroTitulo");
    const lista = document.getElementById("listaSugestoes");

    if (!titulo || !lista) return;
    const nome = titulo.value.trim();
    if (!nome) return;

    lista.innerHTML = "<li>Buscando sugestões...</li>";

    try {
        const resp = await fetch(`${API_SUGGEST}?titulo=${encodeURIComponent(nome)}`);
        if (!resp.ok) { lista.innerHTML = "<li>Sem sugestões.</li>"; return; }

        const json = await resp.json();
        if (!json.Search || json.Search.length === 0) { lista.innerHTML = "<li>Nada encontrado.</li>"; return; }

        lista.innerHTML = "";
        json.Search.forEach(f => {
            const li = document.createElement("li");
            li.classList.add("filme-card-sugestao");
            li.innerHTML = `
                <img src="${f.Poster}" alt="${f.Title}">
                <div class="filme-info-sugestao">
                    <p><strong>${f.Title}</strong> (${f.Year})</p>
                </div>
            `;
            lista.appendChild(li);
        });

    } catch (e) {
        console.error(e);
        lista.innerHTML = "<li>Erro ao sugerir filmes.</li>";
    }
}



async function seguirUsuario(targetId) {
    if (!targetId) return;

    try {
        const resp = await fetch(`${API_USERS}/${meuId}/follow?targetId=${targetId}`, {
            method: "POST"
        });

        if (!resp.ok) {
            alert("Erro ao seguir usuário.");
            return;
        }

        const json = await resp.json();
        alert("Agora você segue: " + json.followed.username);

        // Atualiza a lista de cards
        carregarUsuariosParaSeguir(meuId);

        // Atualiza perfil
        carregarPerfil(meuId);

    } catch (e) {
        console.error(e);
        alert("Erro na requisição.");
    }
}


async function carregarUsuariosParaSeguir(meuId) {
    const container = document.getElementById("procurarUsuarios");

    try {
        const resp = await fetch(API_USERS);
        const data = await resp.json();

        // marca quem já está sendo seguido
        const usuarios = data
            .filter(u => u.id !== meuId)
            .map(u => ({ 
                ...u, 
                seguindo: u.followers && u.followers.some(f => f.id === meuId) 
            }));

        atualizarListaUsuarios("procurarUsuarios", usuarios);

    } catch (e) {
        console.error(e);
        container.innerHTML = "<p>Erro ao carregar usuários.</p>";
    }
}


function atualizarListaUsuarios(containerId, usuarios) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    usuarios.forEach(u => {
        const card = document.createElement("div");
        card.classList.add("usuario-card");

        const info = document.createElement("div");
        info.classList.add("usuario-info");

        const avatar = document.createElement("div");
        avatar.classList.add("usuario-avatar");
        avatar.textContent = u.username[0].toUpperCase();

        const nome = document.createElement("span");
        nome.classList.add("usuario-nome");
        nome.textContent = u.username;

        info.appendChild(avatar);
        info.appendChild(nome);

        const btnSeguir = document.createElement("button");

        if (u.seguindo) {
            btnSeguir.textContent = "Seguindo";
            btnSeguir.disabled = true;
            btnSeguir.classList.add("btn-seguindo");
        } else {
            btnSeguir.textContent = "Seguir";
            btnSeguir.onclick = () => seguirUsuario(u.id);
            btnSeguir.classList.add("btn-seguir");
        }

        card.appendChild(info);
        card.appendChild(btnSeguir);
        container.appendChild(card);
    });
}

// ---- Perfil funcional ----
async function carregarPerfil(id) {
    const respUser = await fetch(`${API_USERS}/${id}`);
    const user = await respUser.json();

    // Preenche infos do perfil
    document.getElementById("perfilNome").textContent = user.username;
    document.getElementById("perfilEmail").textContent = user.email;
    document.getElementById("perfilSeguidores").textContent = `${user.followers?.length || 0} seguidores`;
    document.getElementById("perfilAvatar").textContent = user.username[0].toUpperCase();

    // Carrega listas para modal
    const respSeguindo = await fetch(`${API_USERS}/${id}/following`);
    const seguindo = await respSeguindo.json();
    const listaSeguindo = document.getElementById("modalListSeguindo");
    listaSeguindo.innerHTML = seguindo.length ? seguindo.map(u => `<div>${u.username}</div>`).join('') : "<p>Nenhum seguindo.</p>";

    const respSeguidores = await fetch(`${API_USERS}/${id}/followers`);
    const seguidores = await respSeguidores.json();
    const listaSeguidores = document.getElementById("modalListSeguidores");
    listaSeguidores.innerHTML = seguidores.length ? seguidores.map(u => `<div>${u.username}</div>`).join('') : "<p>Nenhum seguidor.</p>";

    // Carrega sugestões
    carregarUsuariosParaSeguir(id);
}

async function carregarUsuariosParaSeguir(meuId) {
    const container = document.getElementById("procurarUsuarios");
    try {
        const resp = await fetch(API_USERS);
        const data = await resp.json();

        const usuarios = data
            .filter(u => u.id !== meuId)
            .map(u => ({ 
                ...u, 
                seguindo: u.followers && u.followers.some(f => f.id === meuId) 
            }));

        atualizarListaUsuarios("procurarUsuarios", usuarios);
    } catch(e) {
        console.error(e);
        container.innerHTML = "<p>Erro ao carregar usuários.</p>";
    }
}

function atualizarListaUsuarios(containerId, usuarios) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    usuarios.forEach(u => {
        const card = document.createElement("div");
        card.classList.add("usuario-card");

        const info = document.createElement("div");
        info.classList.add("usuario-info");

        const avatar = document.createElement("div");
        avatar.classList.add("usuario-avatar");
        avatar.textContent = u.username[0].toUpperCase();

        const nome = document.createElement("span");
        nome.classList.add("usuario-info-sugestao");
        nome.textContent = u.username;

        info.appendChild(avatar);
        info.appendChild(nome);

        const btnSeguir = document.createElement("button");
        if(u.seguindo) {
            btnSeguir.textContent = "Seguindo";
            btnSeguir.disabled = true;
            btnSeguir.classList.add("btn-seguindo");
        } else {
            btnSeguir.textContent = "Seguir";
            btnSeguir.onclick = () => seguirUsuario(u.id);
            btnSeguir.classList.add("btn-seguir");
        }

        card.appendChild(info);
        card.appendChild(btnSeguir);
        container.appendChild(card);
    });
}

// Modal JS
function abrirModalPerfil() { document.getElementById("modalPerfil").style.display = "flex"; }
function fecharModal() { document.getElementById("modalPerfil").style.display = "none"; }


