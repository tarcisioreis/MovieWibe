MovieWibe

Projeto criado usando Android nativo em Java para uso da API Movie DataBase com um MVP minimo para mostrar em forma de listagem, pesquisa e detalhes os filmes por REST API formatado em JSON.

Foram criadas 3 Activitys sendo a principal a MovieListActivity na qual possui um ListView que irá mostrar os filmes em forma de listagem usando a REST API, recebendo dados em formato JSON, a MovieDetailActivity irá mostrar os detalhes dos filmes e por ultimo a SplashScreenActivity mostra uma tela de abertura do aplicativo.

Será colocado imagens do projeto como a estrutura e as telas do aplicativo que foram testados em emulador Nexus 6 Android 6.0 API 28 e no celular real LG2 Android 5.0 API 21.

Dentro da MovieListActivity terá 3 rotinas que mostrará os filmes em formato de listagem, detalhamento e pesquisa, os dados na listagem mostrados são Título, Lista do Genero do filme, Data de Lancamento e a Imagem do Poster ou Fundo do filme, no detalhamento irá mostrar a Sinopse do filme além dos demais dados mostrados na listagem e por fim a pesquisa por Título podendo ser colocado qualquer texto, o método de pesquisa irá buscar dados da listagem e não encontrando irá usar a pesquisa online por meio da API, o método de detalhamento mostrará os dados e ao selecionar um filme da listagem e o método da listagem monta as informações por meio da API.
