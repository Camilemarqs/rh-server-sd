import amqp from "amqplib";

const RABBIT_URL = process.env.RABBIT_URL ?? "amqp://guest:guest@localhost:5672";
const EXCHANGE = "rh.eventos";
const QUEUE = "rh.eventos.javascript";

function formatarEvento(evento) {
  const dados = Object.entries(evento.dados ?? {})
    .map(([k, v]) => `${k}=${v}`)
    .join(", ");
  return `[FILA] ${evento.tipo} | origem=${evento.origem} | ${dados}`;
}

export async function iniciarConsumidor() {
  const conn = await amqp.connect(RABBIT_URL);
  const ch = await conn.createChannel();

  await ch.assertExchange(EXCHANGE, "fanout", { durable: true });
  const { queue } = await ch.assertQueue(QUEUE, { durable: true });
  await ch.bindQueue(queue, EXCHANGE, "");

  ch.consume(queue, (msg) => {
    if (!msg) return;
    const evento = JSON.parse(msg.content.toString());
    console.log(`\n${formatarEvento(evento)}`);
    ch.ack(msg);
  });

  conn.on("close", () => {
    console.log("  [FILA] Conexão com RabbitMQ encerrada.");
  });

  return conn;
}
