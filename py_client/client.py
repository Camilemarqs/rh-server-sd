import json
import os

import requests

from queue_listener import iniciar_consumidor

BASE_URL = os.environ.get("API_URL", "http://localhost:8080")


def cabecalho(titulo: str):
    print(f"\n{'─' * 50}")
    print(f"  {titulo}")
    print(f"{'─' * 50}")


def exibir_json(data):
    print(json.dumps(data, indent=2, ensure_ascii=False))


def listar_colaboradores():
    cabecalho("Colaboradores cadastrados")
    resp = requests.get(f"{BASE_URL}/colaboradores")
    resp.raise_for_status()
    for c in resp.json():
        print(f"  [{c['tipo']}] id={c['id']} | {c['nome']} | R${c['salario']:.2f}")


def buscar_colaborador():
    id_ = int(input("ID do colaborador: "))
    resp = requests.get(f"{BASE_URL}/colaboradores/{id_}")
    if resp.status_code == 404:
        print(f"  Erro: {resp.json().get('erro')}")
        return
    resp.raise_for_status()
    exibir_json(resp.json())


def adicionar_colaborador():
    print("Tipos: 1=Funcionario  2=Estagiario  3=Autonomo  4=Efetivo")
    tipo = int(input("Tipo: "))
    id_  = int(input("ID: "))
    nome = input("Nome: ")
    sal  = float(input("Salário: ").replace(",", "."))

    if tipo == 1:
        payload = {
            "tipo": "Funcionario", "id": id_, "nome": nome, "salario": sal,
            "cargo": input("Cargo: "),
            "dataAdmissao": input("Data de admissão (dd/MM/yyyy): ")
        }
    elif tipo == 2:
        payload = {
            "tipo": "Estagiario", "id": id_, "nome": nome, "salario": sal,
            "curso": input("Curso: "),
            "cargaHorariaSemanal": int(input("Carga horária semanal: "))
        }
    elif tipo == 3:
        payload = {
            "tipo": "Autonomo", "id": id_, "nome": nome, "salario": sal,
            "especialidade": input("Especialidade: "),
            "cnpj": input("CNPJ: ")
        }
    elif tipo == 4:
        payload = {
            "tipo": "Efetivo", "id": id_, "nome": nome, "salario": sal,
            "cargo": input("Cargo: "),
            "dataAdmissao": input("Data de admissão (dd/MM/yyyy): "),
            "bonusAnual": float(input("Bônus anual: ").replace(",", ".")),
            "anosDeEmpresa": int(input("Anos de empresa: "))
        }
    else:
        print("Tipo inválido.")
        return

    resp = requests.post(f"{BASE_URL}/colaboradores", json=payload)
    if not resp.ok:
        print(f"  Erro: {resp.json().get('erro')}")
        return
    print("  Colaborador adicionado com sucesso!")
    exibir_json(resp.json())


def remover_colaborador():
    id_ = int(input("ID a remover: "))
    resp = requests.delete(f"{BASE_URL}/colaboradores/{id_}")
    data = resp.json()
    if resp.status_code == 404:
        print(f"  Erro: {data.get('erro')}")
    else:
        print(f"  {data.get('mensagem')}")


def custo_colaborador():
    id_ = int(input("ID do colaborador: "))
    resp = requests.get(f"{BASE_URL}/colaboradores/{id_}/custo")
    if resp.status_code == 404:
        print(f"  Erro: {resp.json().get('erro')}")
        return
    resp.raise_for_status()
    data = resp.json()
    print(f"\n  {data['nome']} ({data['tipo']})")
    print(f"  Custo total mensal: R${data['custoTotalMensal']:.2f}")


def listar_departamentos():
    cabecalho("Departamentos")
    resp = requests.get(f"{BASE_URL}/departamentos")
    resp.raise_for_status()
    for d in resp.json():
        gerente = d.get("gerente", {})
        g_nome = gerente.get("nome", "N/A") if gerente else "N/A"
        membros = len(d.get("colaboradores", []))
        print(f"  id={d['id']} | {d['nome']} | gerente={g_nome} | membros={membros}")


def buscar_departamento():
    id_ = int(input("ID do departamento: "))
    resp = requests.get(f"{BASE_URL}/departamentos/{id_}")
    if resp.status_code == 404:
        print(f"  Erro: {resp.json().get('erro')}")
        return
    resp.raise_for_status()
    d = resp.json()
    print(f"\n  Departamento: {d['nome']} (id={d['id']})")
    membros = d.get("colaboradores", [])
    print(f"  Membros ({len(membros)}):")
    for c in membros:
        print(f"    - [{c['tipo']}] {c['nome']}")


def criar_departamento():
    id_   = int(input("ID do novo departamento: "))
    nome  = input("Nome: ")
    payload = {"id": id_, "nome": nome}
    resp = requests.post(f"{BASE_URL}/departamentos", json=payload)
    if not resp.ok:
        print(f"  Erro: {resp.json().get('erro')}")
        return
    print("  Departamento criado com sucesso!")


def adicionar_colaborador_depto():
    id_dept  = int(input("ID do departamento: "))
    id_colab = int(input("ID do colaborador: "))
    resp = requests.post(
        f"{BASE_URL}/departamentos/{id_dept}/colaboradores",
        json={"idColaborador": id_colab}
    )
    if resp.status_code == 404:
        print(f"  Erro: {resp.json().get('erro')}")
        return
    resp.raise_for_status()
    print("  Colaborador adicionado ao departamento!")


def folha_total():
    resp = requests.get(f"{BASE_URL}/folha")
    resp.raise_for_status()
    data = resp.json()
    print(f"\n  Total de colaboradores : {data['totalColaboradores']}")
    print(f"  Folha total mensal     : R${data['folhaTotalMensal']:.2f}")


def folha_departamento():
    id_ = int(input("ID do departamento: "))
    resp = requests.get(f"{BASE_URL}/folha/departamento/{id_}")
    if resp.status_code == 404:
        print(f"  Erro: {resp.json().get('erro')}")
        return
    resp.raise_for_status()
    data = resp.json()
    print(f"\n  Departamento : {data['departamento']}")
    print(f"  Membros      : {data['totalMembros']}")
    print(f"  Folha mensal : R${data['folhaMensal']:.2f}")


def resumo_por_tipo():
    resp = requests.get(f"{BASE_URL}/folha/resumo")
    resp.raise_for_status()
    data = resp.json().get("custoPorTipo", {})
    cabecalho("Custo por tipo de colaborador")
    for tipo, custo in data.items():
        print(f"  {tipo:15s}: R${custo:.2f}")


MENU = """
╔══════════════════════════════════════════════╗
║  Sistema de Gestão de RH — Cliente Python    ║
╠══════════════════════════════════════════════╣
║  COLABORADORES                               ║
║   [1]  Listar colaboradores                  ║
║   [2]  Buscar colaborador por id             ║
║   [3]  Adicionar colaborador                 ║
║   [4]  Remover colaborador                   ║
║   [5]  Custo mensal de um colaborador        ║
║  DEPARTAMENTOS                               ║
║   [6]  Listar departamentos                  ║
║   [7]  Buscar departamento por id            ║
║   [8]  Criar departamento                    ║
║   [9]  Adicionar colaborador a departamento  ║
║  FOLHA DE PAGAMENTO                          ║
║   [10] Folha total da empresa                ║
║   [11] Folha de um departamento              ║
║   [12] Resumo de custos por tipo             ║
║   [0]  Sair                                  ║
╚══════════════════════════════════════════════╝"""

ACOES = {
    1: listar_colaboradores,
    2: buscar_colaborador,
    3: adicionar_colaborador,
    4: remover_colaborador,
    5: custo_colaborador,
    6: listar_departamentos,
    7: buscar_departamento,
    8: criar_departamento,
    9: adicionar_colaborador_depto,
    10: folha_total,
    11: folha_departamento,
    12: resumo_por_tipo,
}


def main():
    print("\nConectando ao servidor em", BASE_URL, "...")
    try:
        requests.get(f"{BASE_URL}/colaboradores", timeout=3)
        print("Conexão REST estabelecida com sucesso!")
    except requests.exceptions.ConnectionError:
        print("ERRO: Servidor não está acessível. Inicie o servidor Java primeiro.")
        return

    try:
        iniciar_consumidor()
        print("Consumidor de fila ativo (eventos assíncronos).")
    except Exception:
        print("AVISO: RabbitMQ indisponível. Comunicação indireta desativada.")

    while True:
        print(MENU)
        try:
            opcao = int(input("Opção: ").strip())
        except ValueError:
            print("  Entrada inválida.")
            continue

        if opcao == 0:
            print("Encerrando cliente Python...")
            break

        acao = ACOES.get(opcao)
        if acao:
            try:
                acao()
            except requests.exceptions.ConnectionError:
                print("  ERRO: Servidor inacessível.")
            except Exception as e:
                print(f"  Erro: {e}")
        else:
            print("  Opção inválida.")


if __name__ == "__main__":
    main()
