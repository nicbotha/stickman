package au.stickman;

import java.util.Collection;
import java.util.concurrent.Callable;

import com.sap.cloud.account.Account;
import com.sap.cloud.account.InvalidTenantException;
import com.sap.cloud.account.Tenant;
import com.sap.cloud.account.TenantAlreadySetException;
import com.sap.cloud.account.TenantContext;

public class TenantContextImpl implements TenantContext
{
	
	public TenantContextImpl() {
		// TODO Auto-generated constructor stub
	}

	public <V> V execute(String arg0, Callable<V> arg1) throws TenantAlreadySetException, InvalidTenantException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAccountName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Tenant> getSubscribedTenants() {
		// TODO Auto-generated method stub
		return null;
	}

	public Tenant getTenant() {
		return new Tenant() {

			public Account getAccount() {
				// TODO Auto-generated method stub
				return null;
			}

			public String getId() {
				return "dev_defaultId";
			}
			
		};
	}

	public String getTenantId() {
		return "dev_defaultId";
	}
}
