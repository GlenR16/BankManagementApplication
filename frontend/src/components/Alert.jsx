export default function Alert({alert}) {
    return (
        <>
            {
                alert && 
                <div className={`alert alert-${alert.type} alert-dismissible rounded-0 fade show`} role="alert">
                    {alert.message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            }
        </>
    )
}
