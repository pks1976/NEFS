
/* this file is generated by com.netspective.sparx.form.Dialog.getSubclassedDialogContextCode(), do not modify (you can extend it, though) */

package auto.dcb.form.exec.inheritance;

import com.netspective.sparx.form.*;
import com.netspective.sparx.form.field.*;
import com.netspective.sparx.form.field.type.*;

public class ValidateContext
{
    public static final String DIALOG_ID = "form.exec.inheritance.validate";
    private DialogContext dialogContext;
    private DialogFieldStates fieldStates;

    public ValidateContext(DialogContext dc)
    {
        this.dialogContext = dc;
        this.fieldStates = dc.getFieldStates();
    }

    public DialogContext getDialogContext() { return dialogContext; }

	public com.netspective.sparx.form.field.type.TextField.TextFieldState getFullNameState() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState) fieldStates.getState("full_name"); }
	public com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue getFullName() { return (com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue) getFullNameState().getValue(); }
	public DialogFieldFlags getFullNameStateFlags() { return getFullNameState().getStateFlags(); }
	public String getFullNamePrivateRequestParam() { return dialogContext.getRequest().getParameter("_dc.full_name"); }
	public String getFullNamePublicRequestParam() { return dialogContext.getRequest().getParameter("full_name"); }
	public com.netspective.sparx.form.field.type.TextField getFullNameField() { return (com.netspective.sparx.form.field.type.TextField) getFullNameState().getField(); }

	public com.netspective.sparx.form.field.type.DateTimeField.DateTimeFieldState getBirthDateState() { return (com.netspective.sparx.form.field.type.DateTimeField.DateTimeFieldState) fieldStates.getState("birth_date"); }
	public com.netspective.sparx.form.field.type.DateTimeField.DateTimeFieldState.DateTimeFieldValue getBirthDate() { return (com.netspective.sparx.form.field.type.DateTimeField.DateTimeFieldState.DateTimeFieldValue) getBirthDateState().getValue(); }
	public DialogFieldFlags getBirthDateStateFlags() { return getBirthDateState().getStateFlags(); }
	public String getBirthDatePrivateRequestParam() { return dialogContext.getRequest().getParameter("_dc.birth_date"); }
	public String getBirthDatePublicRequestParam() { return dialogContext.getRequest().getParameter("birth_date"); }
	public com.netspective.sparx.form.field.type.DateTimeField getBirthDateField() { return (com.netspective.sparx.form.field.type.DateTimeField) getBirthDateState().getField(); }

	public com.netspective.sparx.form.field.type.IntegerField.IntegerFieldState getAgeState() { return (com.netspective.sparx.form.field.type.IntegerField.IntegerFieldState) fieldStates.getState("age"); }
	public com.netspective.sparx.form.field.type.IntegerField.IntegerFieldState.IntegerFieldValue getAge() { return (com.netspective.sparx.form.field.type.IntegerField.IntegerFieldState.IntegerFieldValue) getAgeState().getValue(); }
	public DialogFieldFlags getAgeStateFlags() { return getAgeState().getStateFlags(); }
	public String getAgePrivateRequestParam() { return dialogContext.getRequest().getParameter("_dc.age"); }
	public String getAgePublicRequestParam() { return dialogContext.getRequest().getParameter("age"); }
	public com.netspective.sparx.form.field.type.IntegerField getAgeField() { return (com.netspective.sparx.form.field.type.IntegerField) getAgeState().getField(); }

}
