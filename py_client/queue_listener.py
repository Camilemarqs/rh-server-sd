import json
import os
import threading

import pika

RABBIT_HOST = os.environ.get("RABBIT_HOST", "localhost")
EXCHANGE = "rh.eventos"
QUEUE = "rh.eventos.python"


def _formatar_evento(evento: dict) -> str:
    dados = ", ".join(f"{k}={v}" for k, v in evento.get("dados", {}).items())
    return f"[FILA] {evento['tipo']} | origem={evento['origem']} | {dados}"


def _consumir():
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host=RABBIT_HOST)
    )
    channel = connection.channel()
    channel.exchange_declare(exchange=EXCHANGE, exchange_type="fanout", durable=True)
    channel.queue_declare(queue=QUEUE, durable=True)
    channel.queue_bind(exchange=EXCHANGE, queue=QUEUE)

    def on_message(ch, method, properties, body):
        evento = json.loads(body.decode("utf-8"))
        print(f"\n{_formatar_evento(evento)}")
        ch.basic_ack(delivery_tag=method.delivery_tag)

    channel.basic_consume(queue=QUEUE, on_message_callback=on_message)
    channel.start_consuming()


def iniciar_consumidor():
    thread = threading.Thread(target=_consumir, daemon=True)
    thread.start()
    return thread
