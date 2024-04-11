export default function Alert({alert}) {
    return (
        <>
            {
                alert && 
                <div className={`alert alert-${alert.type} alert-dismissible rounded-0 fade show`} role="alert">
                    {alert.message}
                </div>
            }
        </>
    )
}
