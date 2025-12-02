// =======================================
// CONFIGS DAS APIS
// =======================================
const API_USERS = "http://localhost:8080/users";
const API_LOGIN = "http://localhost:8080/users/login";
const API_FILME = "http://localhost:8080/movies/search";
const API_SUGGEST = "http://localhost:8080/movies/suggest";
const API_REVIEWS = "http://localhost:8080/reviews";

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
    alert("Preenche os campos direito, pá!");
    return;
  }

  try {
    const resp = await fetch(API_USERS, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, email: mail, password: pass }),
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
    data.forEach((u) => {
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
      body: JSON.stringify({ username, password: pass }),
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

// ---- Perfil funcional ----
async function carregarPerfil(id) {
  const respUser = await fetch(`${API_USERS}/${id}`);
  const user = await respUser.json();

  // Preenche infos do perfil
  document.getElementById("perfilNome").textContent = user.username;
  document.getElementById("perfilEmail").textContent = user.email;
  document.getElementById("perfilSeguidores").textContent = `${
    user.followers?.length || 0
  } seguidores`;
  document.getElementById("perfilAvatar").textContent =
    user.username[0].toUpperCase();

  // Carrega listas para modal
  const respSeguindo = await fetch(`${API_USERS}/${id}/following`);
  const seguindo = await respSeguindo.json();
  const listaSeguindo = document.getElementById("modalListSeguindo");
  listaSeguindo.innerHTML = seguindo.length
    ? seguindo
        .map(
          (u) =>
            `<div class="usuario-clicavel" onclick="verPerfilUsuario(${u.id})">${u.username}</div>`
        )
        .join("")
    : "<p>Nenhum seguindo.</p>";

  const respSeguidores = await fetch(`${API_USERS}/${id}/followers`);
  const seguidores = await respSeguidores.json();
  const listaSeguidores = document.getElementById("modalListSeguidores");
  listaSeguidores.innerHTML = seguidores.length
    ? seguidores
        .map(
          (u) =>
            `<div class="usuario-clicavel" onclick="verPerfilUsuario(${u.id})">${u.username}</div>`
        )
        .join("")
    : "<p>Nenhum seguidor.</p>";

  // Carrega avaliações do usuário
  await carregarAvaliacoesUsuario(id, "minhasAvaliacoes");

  // Carrega sugestões
  carregarUsuariosParaSeguir(id);
}

// Carregar avaliações de um usuário
async function carregarAvaliacoesUsuario(userId, containerId) {
  const container = document.getElementById(containerId);
  container.innerHTML = "<p>Carregando avaliações...</p>";

  try {
    const resp = await fetch(`${API_REVIEWS}/user/${userId}`);
    const avaliacoes = await resp.json();

    if (!avaliacoes || avaliacoes.length === 0) {
      container.innerHTML = "<p>Nenhuma avaliação ainda.</p>";
      return;
    }

    container.innerHTML = "";
    avaliacoes.forEach((av) => {
      const card = document.createElement("div");
      card.classList.add("avaliacao-card");

      // Criar estrelas
      const estrelas = "★".repeat(av.nota) + "☆".repeat(5 - av.nota);

      // Imagem do poster (ou placeholder se não tiver)
      const posterUrl = av.moviePoster && av.moviePoster !== "N/A" ? av.moviePoster : "https://via.placeholder.com/150x225?text=Sem+Imagem";

      card.innerHTML = `
        <div class="avaliacao-content">
          <img src="${posterUrl}" alt="${av.movieTitle || 'Filme'}" class="avaliacao-poster" onerror="this.src='https://via.placeholder.com/150x225?text=Sem+Imagem'">
          <div class="avaliacao-info">
            <div class="avaliacao-header">
              <h4>${av.movieTitle || "Filme"}</h4>
              <div class="avaliacao-estrelas">${estrelas}</div>
            </div>
            ${
              av.comentario
                ? `<p class="avaliacao-comentario">${av.comentario}</p>`
                : ""
            }
          </div>
        </div>
      `;
      container.appendChild(card);
    });
  } catch (e) {
    console.error(e);
    container.innerHTML = "<p>Erro ao carregar avaliações.</p>";
  }
}

// Ver perfil de outro usuário
async function verPerfilUsuario(userId) {
  try {
    const respUser = await fetch(`${API_USERS}/${userId}`);
    const user = await respUser.json();

    // Preencher informações do outro usuário
    document.getElementById("outroPerfilNome").textContent = user.username;
    document.getElementById("outroPerfilNomeTitulo").textContent =
      user.username;
    document.getElementById("outroPerfilEmail").textContent = user.email;
    document.getElementById("outroPerfilAvatar").textContent =
      user.username[0].toUpperCase();

    // Carregar seguidores
    const respSeguidores = await fetch(`${API_USERS}/${userId}/followers`);
    const seguidores = await respSeguidores.json();
    document.getElementById(
      "outroPerfilSeguidores"
    ).textContent = `${seguidores.length} seguidores`;

    // Carregar avaliações
    await carregarAvaliacoesUsuario(userId, "avaliacoesOutroUsuario");

    // Abrir tela do outro perfil
    abrirTela("telaPerfilOutro");
  } catch (e) {
    console.error(e);
    alert("Erro ao carregar perfil do usuário.");
  }
}

function voltarParaMeuPerfil() {
  abrirTela("telaPerfil");
  if (meuId) {
    carregarPerfil(meuId);
  }
}

async function carregarUsuariosParaSeguir(meuId) {
  const container = document.getElementById("procurarUsuarios");
  try {
    const resp = await fetch(API_USERS);
    const data = await resp.json();

    const usuarios = data
      .filter((u) => u.id !== meuId)
      .map((u) => ({
        ...u,
        seguindo: u.followers && u.followers.some((f) => f.id === meuId),
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

  usuarios.forEach((u) => {
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

async function seguirUsuario(targetId) {
  if (!targetId) return;

  try {
    const resp = await fetch(
      `${API_USERS}/${meuId}/follow?targetId=${targetId}`,
      {
        method: "POST",
      }
    );

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

// =======================================
// FILMES
// =======================================

// Buscar filme exato
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

// Sugestões quando chamar explicitamente
async function sugerirFilmes() {
  const titulo = document.getElementById("filtroTitulo");
  const lista = document.getElementById("listaSugestoes");

  if (!titulo || !lista) return;
  const nome = titulo.value.trim();
  if (!nome) return;

  lista.innerHTML = "<li>Buscando sugestões...</li>";

  try {
    const resp = await fetch(
      `${API_SUGGEST}?titulo=${encodeURIComponent(nome)}`
    );
    if (!resp.ok) {
      lista.innerHTML = "<li>Sem sugestões.</li>";
      return;
    }

    const json = await resp.json();
    if (!json.Search || json.Search.length === 0) {
      lista.innerHTML = "<li>Nada encontrado.</li>";
      return;
    }

    lista.innerHTML = "";
    json.Search.forEach((f) => {
      const li = document.createElement("li");
      li.classList.add("filme-card-sugestao");
      li.style.cursor = "pointer";
      li.innerHTML = `
                <img src="${f.Poster || ""}" alt="${f.Title}">
                <div class="filme-info-sugestao">
                    <p><strong>${f.Title}</strong> (${f.Year || ""})</p>
                </div>
            `;
      // Adicionar evento de clique para abrir modal de avaliação
      li.onclick = () => abrirModalAvaliacao(f);
      lista.appendChild(li);
    });
  } catch (e) {
    console.error(e);
    lista.innerHTML = "<li>Erro ao sugerir filmes.</li>";
  }
}

// =======================================
// AVALIAÇÕES
// =======================================

let filmeSelecionado = null;
let notaSelecionada = 0;

function abrirModalAvaliacao(filme) {
  if (!meuId) {
    alert("Você precisa estar logado para avaliar um filme!");
    abrirTela("telaLogin");
    return;
  }

  filmeSelecionado = filme;
  notaSelecionada = 0;

  // Preencher informações do filme
  document.getElementById("filmeAvaliacaoPoster").src = filme.Poster || "";
  document.getElementById("filmeAvaliacaoTitulo").textContent =
    filme.Title || "";
  document.getElementById("filmeAvaliacaoAno").textContent = filme.Year || "";
  document.getElementById("comentarioAvaliacao").value = "";
  document.getElementById("notaSelecionada").textContent = "Selecione uma nota";

  // Resetar estrelas
  document.querySelectorAll(".estrela").forEach((estrela, index) => {
    estrela.textContent = "☆";
    estrela.classList.remove("ativa");
  });

  // Abrir modal
  document.getElementById("modalAvaliacao").style.display = "flex";

  // Configurar eventos das estrelas
  configurarEstrelas();
}

function configurarEstrelas() {
  const estrelas = document.querySelectorAll(".estrela");

  estrelas.forEach((estrela, index) => {
    estrela.onmouseenter = () => {
      for (let i = 0; i <= index; i++) {
        estrelas[i].textContent = "★";
        estrelas[i].classList.add("ativa");
      }
      for (let i = index + 1; i < estrelas.length; i++) {
        estrelas[i].textContent = "☆";
        estrelas[i].classList.remove("ativa");
      }
      document.getElementById("notaSelecionada").textContent = `Nota: ${
        index + 1
      }/5`;
    };

    estrela.onclick = () => {
      notaSelecionada = index + 1; // +1 porque index começa em 0
      for (let i = 0; i <= index; i++) {
        estrelas[i].textContent = "★";
        estrelas[i].classList.add("ativa");
      }
      for (let i = index + 1; i < estrelas.length; i++) {
        estrelas[i].textContent = "☆";
        estrelas[i].classList.remove("ativa");
      }
      document.getElementById(
        "notaSelecionada"
      ).textContent = `Nota selecionada: ${notaSelecionada}/5`;
    };
  });

  // Quando sair do hover, manter apenas as selecionadas
  document.getElementById("estrelasContainer").onmouseleave = () => {
    for (let i = 0; i < estrelas.length; i++) {
      if (i < notaSelecionada) {
        // notaSelecionada é 1-5, então i < notaSelecionada
        estrelas[i].textContent = "★";
        estrelas[i].classList.add("ativa");
      } else {
        estrelas[i].textContent = "☆";
        estrelas[i].classList.remove("ativa");
      }
    }
    if (notaSelecionada >= 1) {
      document.getElementById(
        "notaSelecionada"
      ).textContent = `Nota selecionada: ${notaSelecionada}/5`;
    } else {
      document.getElementById("notaSelecionada").textContent =
        "Selecione uma nota (1 a 5 estrelas)";
    }
  };
}

async function salvarAvaliacao() {
  if (!filmeSelecionado || !meuId) {
    alert("Erro: filme ou usuário não identificado!");
    return;
  }

  if (notaSelecionada < 1 || notaSelecionada > 5) {
    alert("Por favor, selecione uma nota de 1 a 5 estrelas!");
    return;
  }

  const comentario = document
    .getElementById("comentarioAvaliacao")
    .value.trim();

  try {
    const response = await fetch(API_REVIEWS, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        imdbId: filmeSelecionado.imdbID,
        comentario: comentario || "",
        nota: notaSelecionada,
        userId: meuId,
      }),
    });

    if (!response.ok) {
      throw new Error("Erro ao salvar avaliação");
    }

    const resultado = await response.json();
    alert("Avaliação salva com sucesso!");
    fecharModalAvaliacao();

    // Recarregar perfil para mostrar a nova avaliação
    if (meuId) {
      await carregarPerfil(meuId);
    }
  } catch (e) {
    console.error(e);
    alert("Erro ao salvar avaliação. Tente novamente.");
  }
}

function fecharModalAvaliacao() {
  document.getElementById("modalAvaliacao").style.display = "none";
  filmeSelecionado = null;
  notaSelecionada = 0;
}

// Modal JS
function abrirModalPerfil() {
  document.getElementById("modalPerfil").style.display = "flex";
}
function fecharModal() {
  document.getElementById("modalPerfil").style.display = "none";
}
