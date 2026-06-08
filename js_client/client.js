import * as readline from "readline";
import { iniciarConsumidor } from "./queue.js";

const BASE_URL = "http://localhost:8080";

const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
const ask = (q) => new Promise((res) => rl.question(q, res));

async function api(method, path, body = null) {
  const opts = {
    method,
    headers: { "Content-Type": "application/json" },
  };
  if (body) opts.body = JSON.stringify(body);
  const resp = await fetch(`${BASE_URL}${path}`, opts);
  const data = await resp.json();
  return { status: resp.status, ok: resp.ok, data };
}

function linha() {
  console.log("─".repeat(50));
}

function exibir(obj) {
  console.log(JSON.stringify(obj, null, 2));
}

async function listarColaboradores() {
  linha();
  const { data } = await api("GET", "/colaboradores");
  for (const c of data) {
    console.log(`  [${c.tipo}] id=${c.id} | ${c.nome} | R$${c.salario.toFixed(2)}`);
  }
}

async function buscarColaborador() {
  const id = await ask("ID do colaborador: ");
  const { status, data } = await api("GET", `/colaboradores/${id}`);
  if (status === 404) { console.log(`  Erro: ${data.erro}`); return; }
  exibir(data);
}

async function adicionarColaborador() {
  console.log("Tipos: 1=Funcionario  2=Estagiario  3=Autonomo  4=Efetivo");
  const tipo = parseInt(await ask("Tipo: "));
  const id   = parseInt(await ask("ID: "));
  const nome = await ask("Nome: ");
  const salario = parseFloat((await ask("Salário: ")).replace(",", "."));

  let payload = { id, nome, salario };

  if (tipo === 1) {
    payload.tipo = "Funcionario";
    payload.cargo = await ask("Cargo: ");
    payload.dataAdmissao = await ask("Data admissão (dd/MM/yyyy): ");
  } else if (tipo === 2) {
    payload.tipo = "Estagiario";
    payload.curso = await ask("Curso: ");
    payload.cargaHorariaSemanal = parseInt(await ask("Carga horária semanal: "));
  } else if (tipo === 3) {
    payload.tipo = "Autonomo";
    payload.especialidade = await ask("Especialidade: ");
    payload.cnpj = await ask("CNPJ: ");
  } else if (tipo === 4) {
    payload.tipo = "Efetivo";
    payload.cargo = await ask("Cargo: ");
    payload.dataAdmissao = await ask("Data admissão (dd/MM/yyyy): ");
    payload.bonusAnual = parseFloat((await ask("Bônus anual: ")).replace(",", "."));
    payload.anosDeEmpresa = parseInt(await ask("Anos de empresa: "));
  } else {
    console.log("  Tipo inválido."); return;
  }

  const { ok, data } = await api("POST", "/colaboradores", payload);
  if (!ok) { console.log(`  Erro: ${data.erro}`); return; }
  console.log("  Colaborador adicionado com sucesso!");
  exibir(data);
}

async function removerColaborador() {
  const id = await ask("ID a remover: ");
  const { status, data } = await api("DELETE", `/colaboradores/${id}`);
  if (status === 404) console.log(`  Erro: ${data.erro}`);
  else console.log(`  ${data.mensagem}`);
}

async function custoColaborador() {
  const id = await ask("ID do colaborador: ");
  const { status, data } = await api("GET", `/colaboradores/${id}/custo`);
  if (status === 404) { console.log(`  Erro: ${data.erro}`); return; }
  console.log(`\n  ${data.nome} (${data.tipo})`);
  console.log(`  Custo total mensal: R$${data.custoTotalMensal.toFixed(2)}`);
}

async function listarDepartamentos() {
  linha();
  const { data } = await api("GET", "/departamentos");
  for (const d of data) {
    const gNome = d.gerente?.nome ?? "N/A";
    const membros = d.colaboradores?.length ?? 0;
    console.log(`  id=${d.id} | ${d.nome} | gerente=${gNome} | membros=${membros}`);
  }
}

async function buscarDepartamento() {
  const id = await ask("ID do departamento: ");
  const { status, data } = await api("GET", `/departamentos/${id}`);
  if (status === 404) { console.log(`  Erro: ${data.erro}`); return; }
  console.log(`\n  Departamento: ${data.nome} (id=${data.id})`);
  const membros = data.colaboradores ?? [];
  console.log(`  Membros (${membros.length}):`);
  for (const c of membros) console.log(`    - [${c.tipo}] ${c.nome}`);
}

async function criarDepartamento() {
  const id   = parseInt(await ask("ID do novo departamento: "));
  const nome = await ask("Nome: ");
  const { ok, data } = await api("POST", "/departamentos", { id, nome });
  if (!ok) { console.log(`  Erro: ${data.erro}`); return; }
  console.log("  Departamento criado com sucesso!");
}

async function adicionarColaboradorDepto() {
  const idDept  = parseInt(await ask("ID do departamento: "));
  const idColab = parseInt(await ask("ID do colaborador: "));
  const { status, data } = await api(
    "POST", `/departamentos/${idDept}/colaboradores`, { idColaborador: idColab }
  );
  if (status === 404) { console.log(`  Erro: ${data.erro}`); return; }
  console.log("  Colaborador adicionado ao departamento!");
}

async function folhaTotal() {
  const { data } = await api("GET", "/folha");
  console.log(`\n  Total de colaboradores : ${data.totalColaboradores}`);
  console.log(`  Folha total mensal     : R$${data.folhaTotalMensal.toFixed(2)}`);
}

async function folhaDepartamento() {
  const id = await ask("ID do departamento: ");
  const { status, data } = await api("GET", `/folha/departamento/${id}`);
  if (status === 404) { console.log(`  Erro: ${data.erro}`); return; }
  console.log(`\n  Departamento : ${data.departamento}`);
  console.log(`  Membros      : ${data.totalMembros}`);
  console.log(`  Folha mensal : R$${data.folhaMensal.toFixed(2)}`);
}

async function resumoPorTipo() {
  const { data } = await api("GET", "/folha/resumo");
  linha();
  console.log("  Custo por tipo de colaborador:");
  for (const [tipo, custo] of Object.entries(data.custoPorTipo)) {
    console.log(`  ${tipo.padEnd(15)}: R$${custo.toFixed(2)}`);
  }
}

const MENU = `
╔══════════════════════════════════════════════════╗
║  Sistema de Gestão de RH — Cliente JavaScript    ║
╠══════════════════════════════════════════════════╣
║  COLABORADORES                                   ║
║   [1]  Listar colaboradores                      ║
║   [2]  Buscar colaborador por id                 ║
║   [3]  Adicionar colaborador                     ║
║   [4]  Remover colaborador                       ║
║   [5]  Custo mensal de um colaborador            ║
║  DEPARTAMENTOS                                   ║
║   [6]  Listar departamentos                      ║
║   [7]  Buscar departamento por id                ║
║   [8]  Criar departamento                        ║
║   [9]  Adicionar colaborador a departamento      ║
║  FOLHA DE PAGAMENTO                              ║
║   [10] Folha total da empresa                    ║
║   [11] Folha de um departamento                  ║
║   [12] Resumo de custos por tipo                 ║
║   [0]  Sair                                      ║
╚══════════════════════════════════════════════════╝`;

const ACOES = {
  1: listarColaboradores,
  2: buscarColaborador,
  3: adicionarColaborador,
  4: removerColaborador,
  5: custoColaborador,
  6: listarDepartamentos,
  7: buscarDepartamento,
  8: criarDepartamento,
  9: adicionarColaboradorDepto,
  10: folhaTotal,
  11: folhaDepartamento,
  12: resumoPorTipo,
};

async function main() {
  console.log(`\nConectando ao servidor em ${BASE_URL} ...`);
  try {
    await fetch(`${BASE_URL}/colaboradores`);
    console.log("Conexão REST estabelecida com sucesso!");
  } catch {
    console.log("ERRO: Servidor não está acessível. Inicie o servidor Java primeiro.");
    rl.close();
    return;
  }

  try {
    await iniciarConsumidor();
    console.log("Consumidor de fila ativo (eventos assíncronos).");
  } catch {
    console.log("AVISO: RabbitMQ indisponível. Comunicação indireta desativada.");
  }

  while (true) {
    console.log(MENU);
    const input = (await ask("Opção: ")).trim();
    const opcao = parseInt(input);

    if (opcao === 0) {
      console.log("Encerrando cliente JavaScript...");
      rl.close();
      break;
    }

    const acao = ACOES[opcao];
    if (acao) {
      try {
        await acao();
      } catch (e) {
        console.log(`  Erro: ${e.message}`);
      }
    } else {
      console.log("  Opção inválida.");
    }
  }
}

main();
