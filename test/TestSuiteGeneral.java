
import com.bankito.dominio.ClienteTest;
import com.bankito.dominio.CuentaTest;
import com.bankito.dominio.MovimientoTest;
import com.bankito.dominio.OperacionTest;
import com.bankito.dominio.PerfilUsuarioTest;
import com.bankito.dominio.TransferenciaTest;
import com.bankito.dominio.UsuarioTest;
import com.bankito.servicio.ServicioBancarioImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ClienteTest.class, UsuarioTest.class, MovimientoTest.class, 
                     CuentaTest.class, TransferenciaTest.class, OperacionTest.class,
                     PerfilUsuarioTest.class, ServicioBancarioImplTest.class})
public class TestSuiteGeneral {
    
}
