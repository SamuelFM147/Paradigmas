public class ConsultaSaldo {

    static String[] clientes = {"Ana", "Bruno", "Carla"};
    static double[] saldos = {1500.00, 500.00, 320.00};

    public static void main(String[] args) {
        System.out.println("=== Caixa eletrônico iniciado ===\n");

        for (int i = 0; i < clientes.length; i++) {
            double saque = 200.00;
            double disponivel;

            System.out.println("Cliente: " + clientes[i] + " | Saque solicitado: R$ " + saque);
            System.out.println("Consultando saldo no servidor do banco...");

            try {
                disponivel = consultarSaldo(i);
                System.out.println("Saldo encontrado: R$ " + disponivel);
            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
                System.out.println("Modo de contingência: saque limitado a R$ 100.0");
                disponivel = 100.00;
            }

            if (saque > disponivel) {
                saque = disponivel;
            }
            System.out.println("Valor liberado: R$ " + saque + "\n");
        }

        System.out.println("O programa continuou sua execução e atendeu todos os clientes.");
        System.out.println("=== Fim do expediente ===");
    }

    static double consultarSaldo(int i) throws Exception {
        if (clientes[i].equals("Bruno")) {
            throw new Exception("Servidor do banco indisponível.");
        }
        return saldos[i];
    }
}
